package com.nexdin.clothingstore.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexdin.clothingstore.domain.enums.EInventoryStatus;
import com.nexdin.clothingstore.domain.enums.ESize;
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
@Table(name = "inventory")
public class Inventory {
    @Id
    private String inventoryID;
    @Enumerated(EnumType.STRING)
    private ESize size;
    private String color;
    private int soldQuantity;
    private int stockQuantity;
    private String imageUrl;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm:ss")
    private LocalDateTime updatedAt;
    @Enumerated(EnumType.STRING)
    private EInventoryStatus inventoryStatus;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Products products;
}
