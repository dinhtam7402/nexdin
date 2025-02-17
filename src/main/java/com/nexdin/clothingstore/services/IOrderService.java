package com.nexdin.clothingstore.services;

import com.nexdin.clothingstore.domain.Orders;
import com.nexdin.clothingstore.payload.request.OrderRequest;
import com.nexdin.clothingstore.repository.IOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface IOrderService {
    Orders create(OrderRequest request);
    Orders update(String orderID, OrderRequest request);
    void delete(String orderID);
    List<Orders> searchOrder(String customerID, String voucherID, String orderDate, String totalAmount, String orderStatus);
}
