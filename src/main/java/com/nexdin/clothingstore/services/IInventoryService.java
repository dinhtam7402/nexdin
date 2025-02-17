package com.nexdin.clothingstore.services;

import com.nexdin.clothingstore.domain.Inventory;
import com.nexdin.clothingstore.domain.enums.EInventoryStatus;
import com.nexdin.clothingstore.domain.enums.ESize;
import com.nexdin.clothingstore.payload.request.InventoryRequest;

import java.util.List;

public interface IInventoryService {
    Inventory getByID(String inventoryID);
    List<Inventory> getBySize(String size);
    List<Inventory> getByColor(String color);
    List<Inventory> getByInventoryStatus(String status);
    List<Inventory> getByProduct(String productID);
    List<Inventory> searchInventory(String size, String color, String status);
    Inventory create(InventoryRequest request);
    Inventory update(String inventoryID, InventoryRequest request);
    void delete(String inventoryID);
}
