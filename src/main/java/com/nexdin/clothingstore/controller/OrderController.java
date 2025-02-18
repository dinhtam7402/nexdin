package com.nexdin.clothingstore.controller;

import com.nexdin.clothingstore.domain.Orders;
import com.nexdin.clothingstore.payload.request.OrderRequest;
import com.nexdin.clothingstore.payload.response.Response;
import com.nexdin.clothingstore.services.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {
    @Autowired
    private IOrderService orderService;

    @PostMapping("/order/create")
    public ResponseEntity<Response<?>> createOrder(@RequestBody OrderRequest request) {
        Orders order = orderService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                Response.builder().status(HttpStatus.CREATED.value())
                        .message("success")
                        .timestamp(LocalDateTime.now())
                        .result(order)
                        .build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/order/{orderID}/status")
    public ResponseEntity<Response<?>> updateOrderStatus(@PathVariable String orderID, @RequestParam String status) {
        Orders order = orderService.updateOrderStatus(orderID, status);
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder().status(HttpStatus.OK.value())
                        .message("success")
                        .result(order)
                        .build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/order/get-all")
    public ResponseEntity<Response<?>> getAllOrder() {
        List<Orders> orders = orderService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder().status(HttpStatus.OK.value())
                        .message("success")
                        .result(orders)
                        .build());
    }

    @GetMapping("/order/get-by-customer/{customerID}")
    public ResponseEntity<Response<?>> getOrderByCustomer(@PathVariable String customerID) {
        List<Orders> orders = orderService.getOrderByCustomer(customerID);
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder().status(HttpStatus.OK.value())
                        .message("success")
                        .result(orders)
                        .build());
    }
}
