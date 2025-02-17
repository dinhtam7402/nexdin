package com.nexdin.clothingstore.services;

import com.nexdin.clothingstore.domain.Products;
import com.nexdin.clothingstore.payload.request.ProductRequest;

import java.util.List;

public interface IProductService {
    Products create(ProductRequest request);
    Products update(String productID, ProductRequest request);
    void deleteByID(String productID);
    Products getByID(String productID);
    List<Products> getAll();
    List<Products> getByProductName(String productName);
    List<Products> getByMaterial(String material);
    List<Products> getByImportPriceBetween(int minPrice, int maxPrice);
    List<Products> getBySellingPriceBetween(int minPrice, int maxPrice);
    List<Products> getByCategoryID(String categoryID);
}
