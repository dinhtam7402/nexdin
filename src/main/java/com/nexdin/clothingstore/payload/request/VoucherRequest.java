package com.nexdin.clothingstore.payload.request;

import lombok.Data;

@Data
public class VoucherRequest {
    private String code;
    private String description;
    private String type;
    private int voucherValue;
    private int minOrderValue;
    private int maxValueAmount;
    private String startDate;
    private String endDate;
    private int usage_limit;
}
