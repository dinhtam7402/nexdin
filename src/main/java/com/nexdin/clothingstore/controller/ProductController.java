package com.nexdin.clothingstore.controller;

import com.nexdin.clothingstore.domain.Products;
import com.nexdin.clothingstore.payload.request.ProductRequest;
import com.nexdin.clothingstore.payload.response.ProductResponse;
import com.nexdin.clothingstore.payload.response.Response;
import com.nexdin.clothingstore.services.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ProductController {
    @Autowired
    private IProductService productService;

    @GetMapping("/product/get-by-id")
    public ResponseEntity<Response<?>> getByID(@RequestParam String productID) {
        Products products = productService.getByID(productID);
        ProductResponse response = new ProductResponse(
                products.getProductID(),
                products.getProductName(),
                products.getDescription(),
                products.getMaterial(),
                products.getSellingPrice(),
                products.getCategories().getCategoryName());

        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder().status(HttpStatus.OK.value())
                        .message("success")
                        .timestamp(LocalDateTime.now())
                        .result(response)
                        .build()
        );
    }

    @GetMapping("/product/get-all")
    public ResponseEntity<Response<?>> getAllProducts() {
        List<Products> products = productService.getAll();

        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder().status(HttpStatus.OK.value())
                        .message("success")
                        .timestamp(LocalDateTime.now())
                        .result(products)
                        .build()
        );
    }

    @GetMapping("/product/get-by-material/{material}")
    public ResponseEntity<Response<?>> getByMaterial(@PathVariable String material) {
        List<Products> products = productService.getByMaterial(material);
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder().status(HttpStatus.OK.value())
                        .message("success")
                        .timestamp(LocalDateTime.now())
                        .result(products)
                        .build()
        );
    }

    @GetMapping("/product/get-by-import-price-between")
    public ResponseEntity<Response<?>> getByImportPriceBetween(@RequestParam int minPrice, @RequestParam int maxPrice) {
        List<Products> products = productService.getByImportPriceBetween(minPrice, maxPrice);
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder().status(HttpStatus.OK.value())
                        .message("success")
                        .timestamp(LocalDateTime.now())
                        .result(products)
                        .build()
        );
    }

    @GetMapping("/product/get-by-selling-price-between")
    public ResponseEntity<Response<?>> getBySellingPriceBetween(@RequestParam int minPrice, @RequestParam int maxPrice) {
        List<Products> products = productService.getBySellingPriceBetween(minPrice, maxPrice);
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder().status(HttpStatus.OK.value())
                        .message("success")
                        .timestamp(LocalDateTime.now())
                        .result(products)
                        .build()
        );
    }

    @GetMapping("/product/get-by-category/{categoryID}")
    public ResponseEntity<Response<?>> getByCategoryID(@PathVariable String categoryID) {
        List<Products> products = productService.getByCategoryID(categoryID);
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder().status(HttpStatus.OK.value())
                        .message("success")
                        .timestamp(LocalDateTime.now())
                        .result(products)
                        .build()
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/product/create")
    public ResponseEntity<Response<?>> createProduct(@RequestBody ProductRequest request) {
        Products products = productService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                Response.builder().status(HttpStatus.CREATED.value())
                        .message("Created successfully")
                        .timestamp(LocalDateTime.now())
                        .result(products)
                        .build()
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/product/update/{productID}")
    public ResponseEntity<Response<?>> updateProduct(@PathVariable String productID, @RequestBody ProductRequest request) {
        Products products = productService.update(productID, request);
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder().status(HttpStatus.OK.value())
                        .message("Updated successfully")
                        .timestamp(LocalDateTime.now())
                        .result(products)
                        .build()
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/product/delete-by-id/{productID}")
    public ResponseEntity<Response<?>> deleteByID(@PathVariable String productID) {
        productService.deleteByID(productID);
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder().status(HttpStatus.OK.value())
                        .message("Deleted successfully")
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}
