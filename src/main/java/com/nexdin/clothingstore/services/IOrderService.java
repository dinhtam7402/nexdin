package com.nexdin.clothingstore.services;

import com.nexdin.clothingstore.domain.Orders;
import com.nexdin.clothingstore.payload.request.OrderRequest;
import com.nexdin.clothingstore.repository.IOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface IOrderService {
    Orders getByID(String orderID);
    Orders create(OrderRequest request);
    Orders updateOrderStatus(String orderID, String status);
    List<Orders> getAll();
    List<Orders> getOrderByCustomer(String customerID);
}
