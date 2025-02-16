package com.nexdin.clothingstore.repository;

import com.nexdin.clothingstore.domain.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICategoryRepository extends JpaRepository<Categories, String> {
    Categories findByCategoryName(String categoryName);
    boolean existsByCategoryName(String categoryName);
}
