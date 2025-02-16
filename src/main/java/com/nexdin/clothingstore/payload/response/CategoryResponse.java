package com.nexdin.clothingstore.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryResponse {
    private String categoryID;
    private String categoryName;
}
