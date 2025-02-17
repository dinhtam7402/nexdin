package com.nexdin.clothingstore.services.impl;

import com.nexdin.clothingstore.domain.Categories;
import com.nexdin.clothingstore.domain.Products;
import com.nexdin.clothingstore.payload.request.ProductRequest;
import com.nexdin.clothingstore.repository.IProductRepository;
import com.nexdin.clothingstore.services.ICategoryService;
import com.nexdin.clothingstore.services.IProductService;
import com.nexdin.clothingstore.utils.IDGenerate;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ProductService implements IProductService {
    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private ICategoryService categoryService;

    @Override
    public Products create(ProductRequest request) {
        if (request.getImportPrice() > request.getSellingPrice()) {
            log.warn("[create] - importPrice cannot be greater than sellingPrice");
            throw new IllegalArgumentException("Invalid input");
        }

        Categories categories = categoryService.getByID(request.getCategoryID());

        Products newProduct = Products.builder()
                .productID(IDGenerate.generate())
                .productName(request.getProductName())
                .description(request.getDescription())
                .material(request.getMaterial())
                .importPrice(request.getImportPrice())
                .sellingPrice(request.getSellingPrice())
                .categories(categories)
                .build();

        productRepository.save(newProduct);
        log.info("[create] - Created product '{}' successfully", request.getProductName());

        return newProduct;
    }

    @Override
    public Products update(String productID, ProductRequest request) {
        Products oldProduct = getByID(productID);

        if (request.getImportPrice() > request.getSellingPrice()) {
            log.warn("[update] - importPrice cannot be greater than sellingPrice");
            throw new IllegalArgumentException("Invalid input");
        }

        oldProduct.setProductName(request.getProductName());
        oldProduct.setDescription(request.getDescription());
        oldProduct.setMaterial(request.getMaterial());
        oldProduct.setImportPrice(request.getImportPrice());
        oldProduct.setSellingPrice(request.getSellingPrice());

        if (request.getCategoryID() != null) {
            Categories categories = categoryService.getByID(request.getCategoryID());
            oldProduct.setCategories(categories);
        }

        productRepository.save(oldProduct);
        log.info("[update] - Updated product ID: {}", productID);

        return oldProduct;
    }

    @Override
    public Products getByID(String productID) {
        Products products = productRepository.findById(productID).orElseThrow(
                () -> {
                    log.warn("[getByID] - Not found product by ID: {}", productID);
                    return new EntityNotFoundException("Not found product by ID: " + productID);
                });
        log.info("[getByID] - Found product by ID: {}", productID);
        return products;
    }

    @Override
    public void deleteByID(String productID) {
        Products products = getByID(productID);
        productRepository.delete(products);
        log.info("[deleteByID] - Deleted product ID '{}' successfully", productID);
    }

    @Override
    public List<Products> getAll() {
        List<Products> products = productRepository.findAll();
        log.info("[getAll] - Retrieved {} products", products.size());
        return products;
    }

    @Override
    public List<Products> getByProductName(String productName) {
        List<Products> products = productRepository.findByProductNameContainingIgnoreCase(productName);
        log.info("[getByProductName] - Retrieved {} products by productName '{}'", products.size(), productName);
        return products;
    }

    @Override
    public List<Products> getByMaterial(String material) {
        List<Products> products = productRepository.findByMaterialContainingIgnoreCase(material);
        log.info("[getByMaterial] - Retrieved {} products by material '{}'", products.size(), material);
        return products;
    }

    @Override
    public List<Products> getByImportPriceBetween(int minPrice, int maxPrice) {
        if (minPrice < 0 || maxPrice < 0 || minPrice > maxPrice) {
            log.warn("[getByImportPriceBetween] - minPrice cannot be greater than maxPrice");
            throw new IllegalArgumentException("Invalid input");
        }

        List<Products> products = productRepository.findByImportPriceBetween(minPrice, maxPrice);
        log.info("[getByImportPriceBetween] - Retrieved {} products by importPriceBetween ({}; {})", products.size(), minPrice, maxPrice);

        return products;
    }

    @Override
    public List<Products> getBySellingPriceBetween(int minPrice, int maxPrice) {
        if (minPrice < 0 || maxPrice < 0 || minPrice > maxPrice) {
            log.warn("[getBySellingPriceBetween] - minPrice cannot be greater than maxPrice");
            throw new IllegalArgumentException("Invalid input");
        }

        List<Products> products = productRepository.findBySellingPriceBetween(minPrice, maxPrice);
        log.info("[getBySellingPriceBetween] - Retrieved {} products by sellingPriceBetween ({}; {})", products.size(), minPrice, maxPrice);

        return products;
    }

    @Override
    public List<Products> getByCategoryID(String categoryID) {
        Categories category = categoryService.getByID(categoryID);

        List<Products> products = productRepository.findByCategoriesCategoryID(categoryID);
        log.info("[getByCategoryID] - Retrieved {} products by categoryID '{}'", products.size(), categoryID);

        return products;
    }
}
