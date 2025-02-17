package com.nexdin.clothingstore.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductResponse {
    private String id;
    private String name;
    private String description;
    private String material;
    private int price;
    private String category;
}
