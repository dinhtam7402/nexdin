package com.nexdin.clothingstore.repository;

import com.nexdin.clothingstore.domain.Products;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IProductRepository extends JpaRepository<Products, String> {
    List<Products> findByProductNameContainingIgnoreCase(String productName);
    List<Products> findByImportPriceBetween(int minPrice, int maxPrice);
    List<Products> findBySellingPriceBetween(int minPrice, int maxPrice);
    List<Products> findByCategoriesCategoryID(String categoryID);
    List<Products> findByCategoriesCategoryName(String categoryName);
    List<Products> findByMaterialContainingIgnoreCase(String material);
}
