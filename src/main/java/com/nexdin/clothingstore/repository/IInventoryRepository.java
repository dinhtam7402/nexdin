package com.nexdin.clothingstore.repository;

import com.nexdin.clothingstore.domain.Inventory;
import com.nexdin.clothingstore.domain.Products;
import com.nexdin.clothingstore.domain.enums.EInventoryStatus;
import com.nexdin.clothingstore.domain.enums.ESize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface IInventoryRepository extends JpaRepository<Inventory, String>, JpaSpecificationExecutor<Inventory> {
    List<Inventory> findByProducts(Products products);
    List<Inventory> findByInventoryStatus(EInventoryStatus status);
    List<Inventory> findBySize(ESize size);
    List<Inventory> findByColor(String color);
}
