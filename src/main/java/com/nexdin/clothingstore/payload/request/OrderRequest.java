package com.nexdin.clothingstore.payload.request;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private String customerID;
    private String voucherCode;
    private List<Items> orderItems;
    private String paymentMethod;

    @Data
    public static class Items {
        private String inventoryID;
        private int quantity;
    }
}
