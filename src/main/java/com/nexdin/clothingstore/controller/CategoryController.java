package com.nexdin.clothingstore.controller;

import com.nexdin.clothingstore.domain.Categories;
import com.nexdin.clothingstore.payload.request.CategoryRequest;
import com.nexdin.clothingstore.payload.response.CategoryResponse;
import com.nexdin.clothingstore.payload.response.Response;
import com.nexdin.clothingstore.services.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;

    @PostMapping("/admin/category/create")
    public ResponseEntity<Response<?>> createCategory(@RequestBody CategoryRequest request) {
        Categories newCategory = categoryService.createCategory(request);
        CategoryResponse response = new CategoryResponse(newCategory.getCategoryID(), newCategory.getCategoryName());
        return ResponseEntity.status(HttpStatus.CREATED).body(
                Response.builder().status(HttpStatus.CREATED.value())
                        .message("Created successfully")
                        .timestamp(LocalDateTime.now())
                        .result(response)
                        .build()
        );
    }

    @PutMapping("/admin/category/update/{categoryID}")
    public ResponseEntity<Response<?>> updateCategory(@PathVariable String categoryID, @RequestBody CategoryRequest request) {
        Categories categories = categoryService.updateCategory(categoryID, request);
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder().status(HttpStatus.OK.value())
                        .message("Updated successfully")
                        .timestamp(LocalDateTime.now())
                        .result(categories)
                        .build()
        );
    }

    @DeleteMapping("/admin/category/delete-by-id")
    public ResponseEntity<Response<?>> deleteByID(@RequestParam String categoryID) {
        categoryService.deleteCategory(categoryID);
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder().status(HttpStatus.OK.value())
                        .message("Deleted successfully")
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @DeleteMapping("/admin/category/delete-by-name")
    public ResponseEntity<Response<?>> deleteByName(@RequestParam String categoryName) {
        categoryService.deleteByCategoryName(categoryName);
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder().status(HttpStatus.OK.value())
                        .message("Deleted successfully")
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping("/category/get-category-by-name")
    public ResponseEntity<Response<?>> getByCategoryName(@RequestParam String categoryName) {
        Categories categories = categoryService.getByCategoryName(categoryName);
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder()
                        .status(HttpStatus.OK.value())
                        .message("Get category by name successfully")
                        .timestamp(LocalDateTime.now())
                        .result(categories)
                        .build()
        );
    }

    @GetMapping("/category/get-all")
    public ResponseEntity<Response<?>> getAllCategories() {
        List<Categories> categories = categoryService.getAllCategories();
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder().status(HttpStatus.OK.value())
                        .message("Get all successfully")
                        .timestamp(LocalDateTime.now())
                        .result(categories)
                        .build()
        );
    }

    @GetMapping("/category/get-by-id")
    public ResponseEntity<Response<?>> getById(@RequestParam String categoryID) {
        Categories categories = categoryService.getByID(categoryID);
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder().status(HttpStatus.OK.value())
                        .message("Get by id successfully")
                        .timestamp(LocalDateTime.now())
                        .result(categories)
                        .build()
        );
    }
}
