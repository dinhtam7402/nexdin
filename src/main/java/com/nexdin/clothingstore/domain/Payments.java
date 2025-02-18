package com.nexdin.clothingstore.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexdin.clothingstore.domain.enums.EPaymentMethod;
import com.nexdin.clothingstore.domain.enums.EPaymentStatus;
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
@Table(name = "payments")
public class Payments {
    @Id
    private String paymentID;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm:ss")
    private LocalDateTime paymentDate;
    @Enumerated(EnumType.STRING)
    private EPaymentMethod paymentMethod;
    @Enumerated(EnumType.STRING)
    private EPaymentStatus paymentStatus;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Orders orders;
}
