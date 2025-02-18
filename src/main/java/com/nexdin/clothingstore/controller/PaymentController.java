package com.nexdin.clothingstore.controller;

import com.nexdin.clothingstore.domain.Payments;
import com.nexdin.clothingstore.payload.response.Response;
import com.nexdin.clothingstore.services.IPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class PaymentController {
    @Autowired
    private IPaymentService paymentService;

    @PutMapping("/admin/payment/{paymentID}/status")
    public ResponseEntity<Response<?>> confirmPayment(@PathVariable String paymentID, @RequestParam String status) {
        Payments payment = paymentService.confirmPayment(paymentID,  status);
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder().status(HttpStatus.OK.value())
                        .message("success")
                        .timestamp(LocalDateTime.now())
                        .result(payment)
                        .build());
    }
}
