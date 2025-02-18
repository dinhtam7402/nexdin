package com.nexdin.clothingstore.controller;

import com.nexdin.clothingstore.domain.Customers;
import com.nexdin.clothingstore.payload.request.CustomerRequest;
import com.nexdin.clothingstore.payload.response.Response;
import com.nexdin.clothingstore.services.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CustomerController {
    @Autowired
    private ICustomerService customerService;

    @GetMapping("/customer/get-by-id/{customerID}")
    public ResponseEntity<Response<?>> getByID(@PathVariable String customerID) {
        Customers customer = customerService.getByID(customerID);
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder().status(HttpStatus.OK.value())
                        .message("success")
                        .timestamp(LocalDateTime.now())
                        .result(customer)
                        .build());
    }

    @GetMapping("/customer/get-all")
    public ResponseEntity<Response<?>> getAll() {
        List<Customers> customers = customerService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder().status(HttpStatus.OK.value())
                        .message("success")
                        .timestamp(LocalDateTime.now())
                        .result(customers)
                        .build());
    }

    @GetMapping("/customer/get-by-name")
    public ResponseEntity<Response<?>> getByFullName(@RequestParam String fullName) {
        List<Customers> customers = customerService.getByFullName(fullName);
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder().status(HttpStatus.OK.value())
                        .message("success")
                        .timestamp(LocalDateTime.now())
                        .result(customers)
                        .build());
    }

    @GetMapping("/customer/get-by-email")
    public ResponseEntity<Response<?>> getByEmail(@RequestParam String email) {
        List<Customers> customers = customerService.getByEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder().status(HttpStatus.OK.value())
                        .message("success")
                        .timestamp(LocalDateTime.now())
                        .result(customers)
                        .build());
    }

    @GetMapping("/customer/get-by-address")
    public ResponseEntity<Response<?>> getByAddress(@RequestParam String address) {
        List<Customers> customers = customerService.getByAddress(address);
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder().status(HttpStatus.OK.value())
                        .message("success")
                        .timestamp(LocalDateTime.now())
                        .result(customers)
                        .build());
    }

    @PostMapping("/customer/create")
    public ResponseEntity<Response<?>> createCustomer(@RequestBody CustomerRequest request) {
        Customers customer = customerService.create(request);
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder().status(HttpStatus.OK.value())
                        .message("success")
                        .timestamp(LocalDateTime.now())
                        .result(customer)
                        .build());
    }

    @PutMapping("/customer/update/{customerID}")
    public ResponseEntity<Response<?>> updateCustomer(@PathVariable String customerID, @RequestBody CustomerRequest request) {
        Customers customer = customerService.update(customerID, request);
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder().status(HttpStatus.OK.value())
                        .message("success")
                        .timestamp(LocalDateTime.now())
                        .result(customer)
                        .build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/customer/delete/{customerID}")
    public ResponseEntity<Response<?>> deleteCustomer(@PathVariable String customerID) {
        customerService.delete(customerID);
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder().status(HttpStatus.OK.value())
                        .message("success")
                        .timestamp(LocalDateTime.now())
                        .build());
    }
}
