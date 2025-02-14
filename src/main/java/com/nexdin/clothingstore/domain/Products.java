package com.nexdin.clothingstore.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
public class Products {
    @Id
    private String productID;
    private String productName;
    private String description;
    private String material;
    private int importPrice;
    private int sellingPrice;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Categories categories;
}
