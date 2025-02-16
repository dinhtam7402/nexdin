package com.nexdin.clothingstore.services;

import com.nexdin.clothingstore.domain.Categories;
import com.nexdin.clothingstore.payload.request.CategoryRequest;

import java.util.List;

public interface ICategoryService {
    // them category moi (C)
    Categories createCategory(CategoryRequest request);
    boolean existsByCategoryName(String categoryName);
    // lay danh sach category (R)
    List<Categories> getAllCategories();
    // lay category bang categoryName
    Categories getByCategoryName(String categoryName);
    // lay category bang id
    Categories getByID(String categoryID);
    // cap nhat thong tin category (U)
    Categories updateCategory(String categoryID, CategoryRequest request);
    // xoa category (D)
    void deleteCategory(String categoryID);
    void deleteByCategoryName(String categoryName);
}
