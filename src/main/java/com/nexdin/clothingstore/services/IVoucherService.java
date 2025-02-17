package com.nexdin.clothingstore.services;

import com.nexdin.clothingstore.domain.Vouchers;
import com.nexdin.clothingstore.domain.enums.EVoucherStatus;
import com.nexdin.clothingstore.domain.enums.EVoucherType;
import com.nexdin.clothingstore.payload.request.VoucherRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface IVoucherService {
    Vouchers create(VoucherRequest request);
    Vouchers update(String voucherID, VoucherRequest request);
    void deleteByID(String voucherID);
    void deleteByCode(String code);
    void  disableVoucher(String voucherID);
    void existByCode(String code);
    Vouchers getByID(String voucherID);
    Vouchers getByCode(String code);
    List<Vouchers> getAll();
    List<Vouchers> searchVoucher(String type, String voucherValue, String minOrderValue, String maxValueAmount, String startDate, String endDate, String usage_limit, String used_count, String status);
    int applyVoucher(String voucherCode, int orderValue);
}
