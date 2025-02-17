package com.nexdin.clothingstore.services.impl;

import com.nexdin.clothingstore.domain.*;
import com.nexdin.clothingstore.domain.enums.EInventoryStatus;
import com.nexdin.clothingstore.domain.enums.EOrderStatus;
import com.nexdin.clothingstore.payload.request.OrderRequest;
import com.nexdin.clothingstore.repository.IOrderRepository;
import com.nexdin.clothingstore.services.*;
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

    @Override
    @Transactional
    public Orders create(OrderRequest request) {
        Customers customer = customerService.getByID(request.getCustomerID());

        Orders newOrder = new Orders();
        newOrder.setOrderID(IDGenerate.generate());
        newOrder.setCustomers(customer);
        newOrder.setOrderDate(LocalDateTime.now());
        newOrder.setOrderStatus(EOrderStatus.PENDING);

        int totalAmount = 0;
        List<OrderItems> orderItems = new ArrayList<>();

        for (OrderRequest.Items item : request.getOrderItems()) {
            Inventory inventory = inventoryService.getAndLockByID(item.getInventoryID());
            if (item.getQuantity() > inventory.getStockQuantity() || inventory.getInventoryStatus() != EInventoryStatus.IN_STOCK || inventory.getStockQuantity() <= 0) {
                log.warn("[create] - Product '{}' is not enough in stock", inventory.getProducts().getProductName());
                throw new EntityNotFoundException("Product " + inventory.getProducts().getProductName() + " is not enough in stock");
            }

            OrderItems orderItem = new OrderItems(IDGenerate.generate(), item.getQuantity(), inventory.getProducts().getSellingPrice(), newOrder,  inventory.getProducts());
            orderItems.add(orderItem);
            totalAmount += orderItem.getQuantity() * orderItem.getPrice();
        }

        Vouchers voucher = voucherService.getAndLockByCode(request.getVoucherCode());
        Integer discountAmount = voucherService.applyVoucher(voucher, totalAmount);
        if (discountAmount != null) {
            totalAmount -= discountAmount;
            newOrder.setVouchers(voucher);
        } else {
            newOrder.setVouchers(null);
        }

        newOrder.setTotalAmount(totalAmount);

        for (OrderRequest.Items item : request.getOrderItems()) {
            Inventory inventory = inventoryService.getByID(item.getInventoryID());
            inventory.setStockQuantity(inventory.getStockQuantity() - item.getQuantity());
            inventory.setSoldQuantity(inventory.getSoldQuantity() + item.getQuantity());
            if (inventory.getStockQuantity() <= 0) inventory.setInventoryStatus(EInventoryStatus.OUT_OF_STOCK);
            inventoryService.save(inventory);
        }

        newOrder.setOrderItems(orderItems);
        orderRepository.save(newOrder);

        return newOrder;
    }

    @Override
    public Orders update(String orderID, OrderRequest request) {
        return null;
    }

    @Override
    public void delete(String orderID) {

    }

    @Override
    public List<Orders> searchOrder(String customerID, String voucherID, String orderDate, String totalAmount, String orderStatus) {
        return List.of();
    }
}
