package com.nexdin.clothingstore.payload.request;

import lombok.Data;

@Data
public class ProductRequest {
    private String productName;
    private String description;
    private String material;
    private int importPrice;
    private int sellingPrice;
    private String categoryID;
}
