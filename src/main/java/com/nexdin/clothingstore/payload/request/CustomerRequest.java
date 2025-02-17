package com.nexdin.clothingstore.payload.request;

import lombok.Data;

@Data
public class CustomerRequest {
    private String fullName;
    private String email;
    private String phone;
    private String address;
}
