package com.nexdin.clothingstore.controller;

import com.nexdin.clothingstore.domain.Vouchers;
import com.nexdin.clothingstore.payload.request.VoucherRequest;
import com.nexdin.clothingstore.payload.response.Response;
import com.nexdin.clothingstore.services.IVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class VoucherController {
    @Autowired
    private IVoucherService voucherService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/voucher/create")
    public ResponseEntity<Response<?>> createVoucher(@RequestBody VoucherRequest request) {
        Vouchers voucher = voucherService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                Response.builder().status(HttpStatus.CREATED.value())
                        .message("Created success")
                        .timestamp(LocalDateTime.now())
                        .result(voucher)
                        .build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/voucher/update/{voucherID}")
    public ResponseEntity<Response<?>> updateVoucher(@PathVariable String voucherID, @RequestBody VoucherRequest request) {
        Vouchers voucher = voucherService.update(voucherID, request);
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder().status(HttpStatus.OK.value())
                        .message("Updated success")
                        .timestamp(LocalDateTime.now())
                        .result(voucher)
                        .build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/voucher/delete/{voucherID}")
    public ResponseEntity<Response<?>> deleteVoucher(@PathVariable String voucherID) {
        voucherService.deleteByID(voucherID);
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder().status(HttpStatus.OK.value())
                        .message("Deleted success")
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/voucher/disable/{voucherID}")
    public ResponseEntity<Response<?>> disableVoucher(@PathVariable String voucherID) {
        voucherService.disableVoucher(voucherID);
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder().status(HttpStatus.OK.value())
                        .message("Disabled success")
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @GetMapping("/voucher/get-by-id/{voucherID}")
    public ResponseEntity<Response<?>> getByID(@PathVariable String voucherID) {
        Vouchers voucher = voucherService.getByID(voucherID);
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder().status(HttpStatus.OK.value())
                        .message("success")
                        .timestamp(LocalDateTime.now())
                        .result(voucher)
                        .build());
    }

    @GetMapping("/voucher/get-by-code/{code}")
    public ResponseEntity<Response<?>> getByCode(@PathVariable String code) {
        Vouchers voucher = voucherService.getByCode(code);
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder().status(HttpStatus.OK.value())
                        .message("success")
                        .timestamp(LocalDateTime.now())
                        .result(voucher)
                        .build());
    }

    @GetMapping("/voucher/get-all")
    public ResponseEntity<Response<?>> getAll() {
        List<Vouchers> vouchers = voucherService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder().status(HttpStatus.OK.value())
                        .message("success")
                        .timestamp(LocalDateTime.now())
                        .result(vouchers)
                        .build());
    }

    @GetMapping("/voucher/search")
    public ResponseEntity<Response<?>> searchVoucher(@RequestParam(required = false) String type,
                                                     @RequestParam(required = false) String voucherValue,
                                                     @RequestParam(required = false) String minOrderValue,
                                                     @RequestParam(required = false) String maxValueAmount,
                                                     @RequestParam(required = false) String startDate,
                                                     @RequestParam(required = false) String endDate,
                                                     @RequestParam(required = false) String usage_limit,
                                                     @RequestParam(required = false) String used_count,
                                                     @RequestParam(required = false) String status) {
        List<Vouchers> vouchers = voucherService.searchVoucher(
                type,
                voucherValue,
                minOrderValue,
                maxValueAmount,
                startDate, endDate,
                usage_limit,
                used_count,
                status);
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder().status(HttpStatus.OK.value())
                        .message("success")
                        .timestamp(LocalDateTime.now())
                        .result(vouchers)
                        .build());
    }
}
