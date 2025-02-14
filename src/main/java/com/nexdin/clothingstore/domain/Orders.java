package com.nexdin.clothingstore.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexdin.clothingstore.domain.enums.EOrderStatus;
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
@Table(name = "orders")
public class Orders {
    @Id
    private String orderID;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm:ss")
    private LocalDateTime orderDate;
    private int totalAmount;
    @Enumerated(EnumType.STRING)
    private EOrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customers customers;
    @ManyToOne
    @JoinColumn(name = "voucher_id")
    private Vouchers vouchers;
}
