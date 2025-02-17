package com.nexdin.clothingstore.payload.request;

import lombok.Data;

@Data
public class InventoryRequest {
    private String size;
    private String color;
    private int stockQuantity;
    private String inventoryStatus;
    private String productID;
    private String imageUrl;
}
