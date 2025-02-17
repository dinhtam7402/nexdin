package com.nexdin.clothingstore.controller;

import com.nexdin.clothingstore.domain.Orders;
import com.nexdin.clothingstore.payload.request.OrderRequest;
import com.nexdin.clothingstore.payload.response.Response;
import com.nexdin.clothingstore.services.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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
}
