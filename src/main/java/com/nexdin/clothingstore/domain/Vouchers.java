package com.nexdin.clothingstore.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexdin.clothingstore.domain.enums.EVoucherStatus;
import com.nexdin.clothingstore.domain.enums.EVoucherType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vouchers")
public class Vouchers {
    @Id
    private String voucherID;
    private String code;
    private String description;
    @Enumerated(EnumType.STRING)
    private EVoucherType voucherType;
    private int voucherValue;
    private int minOrderValue;
    private int maxValueAmount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm:ss")
    private LocalDateTime startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm:ss")
    private LocalDateTime endDate;
    private int usage_limit;
    private int used_count;
    @Enumerated(EnumType.STRING)
    private EVoucherStatus voucherStatus;
}
