package com.nexdin.clothingstore.services.impl;

import com.nexdin.clothingstore.domain.Categories;
import com.nexdin.clothingstore.exception.DuplicateResourceException;
import com.nexdin.clothingstore.payload.request.CategoryRequest;
import com.nexdin.clothingstore.repository.ICategoryRepository;
import com.nexdin.clothingstore.services.ICategoryService;
import com.nexdin.clothingstore.utils.IDGenerate;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CategoryService implements ICategoryService {
    @Autowired
    private ICategoryRepository categoryRepository;

    @Override
    public boolean existsByCategoryName(String categoryName) {
        return categoryRepository.existsByCategoryName(categoryName);
    }

    @Override
    public Categories createCategory(CategoryRequest request) {
        boolean exist = existsByCategoryName(request.getCategoryName());

        if (exist) {
            log.warn("[createCategory] - Category '{}' already exists", request.getCategoryName());
            throw new DuplicateResourceException("Category " + request.getCategoryName() + " already exists");
        }

        Categories newCategory = new Categories(IDGenerate.generate(), request.getCategoryName());

        return categoryRepository.save(newCategory);
    }

    @Override
    public List<Categories> getAllCategories() {
        List<Categories> categories = categoryRepository.findAll();
        log.info("Retrieved {} categories", categories.size());
        return categories;
    }

    @Override
    public Categories getByCategoryName(String categoryName) {
        Categories category = categoryRepository.findByCategoryName(categoryName);
        if (category == null) {
            log.warn("[getByCategoryName] - Category not found: '{}'", categoryName);
            throw new EntityNotFoundException("Category not found: " + categoryName);
        }
        log.info("Category found by name: '{}'", categoryName);
        return category;
    }

    @Override
    public Categories getByID(String categoryID) {
        Categories categories = categoryRepository.findById(categoryID).orElseThrow(
                () -> {
                    log.warn("[getByID] - Category not found: '{}'", categoryID);
                    return new EntityNotFoundException("Category not found: " + categoryID);
                }
        );
        log.info("Category found by id: '{}'", categoryID);
        return categories;
    }

    @Override
    public Categories updateCategory(String categoryID, CategoryRequest request) {
        Categories oldCategory = getByID(categoryID);

        oldCategory.setCategoryName(request.getCategoryName());
        categoryRepository.save(oldCategory);
        log.info("Updated category '{}' successfully", categoryID);

        return oldCategory;
    }

    @Override
    public void deleteCategory(String categoryID) {
        if (categoryRepository.existsById(categoryID)) {
            categoryRepository.deleteById(categoryID);
            log.info("[deleteCategory] - Deleted category id '{}' successfully", categoryID);
        } else {
            log.info("[deleteCategory] - Category id '{}' not exist", categoryID);
            throw new EntityNotFoundException("Category id " + categoryID + " not exist");
        }

    }

    @Override
    public void deleteByCategoryName(String categoryName) {
        Categories category = getByCategoryName(categoryName);
        categoryRepository.delete(category);
        log.info("[deleteByCategoryName] - Deleted category id '{}' successfully", categoryName);
    }
}
