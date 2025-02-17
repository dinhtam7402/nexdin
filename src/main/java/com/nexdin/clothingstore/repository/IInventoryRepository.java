package com.nexdin.clothingstore.repository;

import com.nexdin.clothingstore.domain.Inventory;
import com.nexdin.clothingstore.domain.Products;
import com.nexdin.clothingstore.domain.enums.EInventoryStatus;
import com.nexdin.clothingstore.domain.enums.ESize;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IInventoryRepository extends JpaRepository<Inventory, String>, JpaSpecificationExecutor<Inventory> {
    List<Inventory> findByProducts(Products products);
    List<Inventory> findByInventoryStatus(EInventoryStatus status);
    List<Inventory> findBySize(ESize size);
    List<Inventory> findByColor(String color);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM Inventory i WHERE i.inventoryID = :inventoryID")
    Inventory findAndLockById(@Param("inventoryID") String inventoryID);
}
