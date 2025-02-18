package com.nexdin.clothingstore.services.impl;

import com.nexdin.clothingstore.domain.*;
import com.nexdin.clothingstore.domain.enums.EInventoryStatus;
import com.nexdin.clothingstore.domain.enums.EOrderStatus;
import com.nexdin.clothingstore.domain.enums.EPaymentMethod;
import com.nexdin.clothingstore.domain.enums.EPaymentStatus;
import com.nexdin.clothingstore.payload.request.OrderRequest;
import com.nexdin.clothingstore.repository.IOrderRepository;
import com.nexdin.clothingstore.services.*;
import com.nexdin.clothingstore.utils.FactoryEnum;
import com.nexdin.clothingstore.utils.IDGenerate;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class OrderService implements IOrderService {
    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private IVoucherService voucherService;

    @Autowired
    private IInventoryService inventoryService;

    @Autowired
    private IPaymentService paymentService;

    @Override
    public Orders getByID(String orderID) {
        Orders order = orderRepository.findById(orderID).orElseThrow(
                () -> {
                    log.warn("[getByID] - Not found order by ID: {}", orderID);
                    return new EntityNotFoundException("Not found order by ID: " + orderID);
                });
        log.info("[getByID] - Found order by ID: {}", orderID);
        return order;
    }

    @Override
    @Transactional
    public Orders create(OrderRequest request) {
        // Lấy thông tin khách hàng
        Customers customer = customerService.getByID(request.getCustomerID());

        // Tạo đơn đặt hàng
        Orders newOrder = new Orders();
        newOrder.setOrderID(IDGenerate.generate());
        newOrder.setCustomers(customer);
        newOrder.setOrderDate(LocalDateTime.now());
        newOrder.setOrderStatus(EOrderStatus.PENDING);

        int totalAmount = 0;
        List<OrderItems> orderItems = new ArrayList<>();

        // Kiểm tra hàng tồn kho
        for (OrderRequest.Items item : request.getOrderItems()) {
            Inventory inventory = inventoryService.getAndLockByID(item.getInventoryID());

            // Nếu số lượng không đủ thì ném ra ngoại lệ
            if (item.getQuantity() > inventory.getStockQuantity() || inventory.getInventoryStatus() != EInventoryStatus.IN_STOCK || inventory.getStockQuantity() <= 0) {
                log.warn("[create] - Product '{}' is not enough in stock", inventory.getProducts().getProductName());
                throw new EntityNotFoundException("Product " + inventory.getProducts().getProductName() + " is not enough in stock");
            }

            // Đủ số lượng tạo danh sách sản phẩm được đặt và tính tổng giá trị đơn đặt hàng
            OrderItems orderItem = new OrderItems(IDGenerate.generate(), item.getQuantity(), inventory.getProducts().getSellingPrice(), newOrder,  inventory.getProducts());
            orderItems.add(orderItem);
            totalAmount += orderItem.getQuantity() * orderItem.getPrice();

            // Cập nhật số lượng đã bán và tồn kho
            inventory.setStockQuantity(inventory.getStockQuantity() - item.getQuantity());
            inventory.setSoldQuantity(inventory.getSoldQuantity() + item.getQuantity());
            if (inventory.getStockQuantity() <= 0) inventory.setInventoryStatus(EInventoryStatus.OUT_OF_STOCK);
            inventoryService.save(inventory);
        }

        // Sử dụng voucher
        Vouchers voucher = voucherService.getAndLockByCode(request.getVoucherCode());
        Integer discountAmount = voucherService.applyVoucher(voucher, totalAmount);
        if (discountAmount != null) {
            totalAmount -= discountAmount;
            newOrder.setVouchers(voucher);
        } else {
            newOrder.setVouchers(null);
        }

        newOrder.setTotalAmount(totalAmount);
        newOrder.setOrderItems(orderItems);
        orderRepository.save(newOrder);

        Payments payment = new Payments();
        payment.setPaymentID(IDGenerate.generate());
        payment.setOrders(newOrder);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setPaymentStatus(EPaymentStatus.UNPAID);
        if (request.getPaymentMethod() == null || request.getPaymentMethod().isEmpty()) {
            payment.setPaymentMethod(EPaymentMethod.COD);
        } else {
            payment.setPaymentMethod(FactoryEnum.getEnumInstance(EPaymentMethod.class, request.getPaymentMethod()));
        }
        paymentService.create(payment);

        return newOrder;
    }

    @Override
    public Orders updateOrderStatus(String orderID, String status) {
        Orders order = getByID(orderID);
        order.setOrderStatus(FactoryEnum.getEnumInstance(EOrderStatus.class, status));
        log.info("[updateOrderStatus] - Updated order status successfully");
        return order;
    }

    @Override
    public List<Orders> getAll() {
        List<Orders> orders = orderRepository.findAll();
        log.info("[getAll] - Retrieved {} orders", orders.size());
        return orders;
    }

    @Override
    public List<Orders> getOrderByCustomer(String customerID) {
        Customers customer = customerService.getByID(customerID);
        List<Orders> orders = orderRepository.findByCustomers(customer);
        log.info("[getOrderByCustomer] - Retrieved {} orders", orders.size());
        return orders;
    }
}
