package com.nexdin.clothingstore.services.impl;

import com.nexdin.clothingstore.domain.Inventory;
import com.nexdin.clothingstore.domain.Products;
import com.nexdin.clothingstore.domain.enums.EInventoryStatus;
import com.nexdin.clothingstore.domain.enums.ESize;
import com.nexdin.clothingstore.payload.request.InventoryRequest;
import com.nexdin.clothingstore.repository.IInventoryRepository;
import com.nexdin.clothingstore.services.IInventoryService;
import com.nexdin.clothingstore.services.IProductService;
import com.nexdin.clothingstore.utils.FactoryEnum;
import com.nexdin.clothingstore.utils.IDGenerate;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class InventoryService implements IInventoryService {
    @Autowired
    private IInventoryRepository inventoryRepository;

    @Autowired
    private IProductService productService;

    @Override
    public Inventory getByID(String inventoryID) {
        Inventory inventory = inventoryRepository.findById(inventoryID).orElseThrow(
                () -> {
                    log.warn("[getByID] - Not found inventory with ID: {}", inventoryID);
                    return new EntityNotFoundException("Not found inventory with ID: " + inventoryID);
                });

        log.info("[getByID] - Found inventory with ID: {}", inventoryID);
        return inventory;
    }

    @Override
    public List<Inventory> getBySize(String size) {
        List<Inventory> inventories = inventoryRepository.findBySize(FactoryEnum.getEnumInstance(ESize.class, size));
        log.info("[getBySize] - Retrieved {} inventory by size", inventories.size());
        return inventories;
    }

    @Override
    public List<Inventory> getByColor(String color) {
        if (color == null || color.isEmpty()) {
            log.warn("[getByColor] - Color cannot be null or empty");
            throw new IllegalArgumentException("Color cannot be null or empty");
        }

        List<Inventory> inventories = inventoryRepository.findByColor(color);
        log.info("[getByColor] - Retrieved {} inventory by color", inventories.size());

        return inventories;
    }

    @Override
    public List<Inventory> getByInventoryStatus(String status) {
        List<Inventory> inventories = inventoryRepository.findByInventoryStatus(FactoryEnum.getEnumInstance(EInventoryStatus.class, status));
        log.info("[getByInventoryStatus] - Retrieved {} inventory by status", inventories.size());
        return inventories;
    }

    @Override
    public List<Inventory> getByProduct(String productID) {
        Products products = productService.getByID(productID);
        List<Inventory> inventories = inventoryRepository.findByProducts(products);
        log.info("[getByProduct] - Retrieved {} inventory by product", inventories.size());
        return inventories;
    }

    @Override
    public List<Inventory> searchInventory(String size, String color, String status) {
        Specification<Inventory> spec = ((root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (size != null) predicates.add(builder.equal(root.get("size"), FactoryEnum.getEnumInstance(ESize.class, size)));
            if (color != null && !color.isEmpty()) predicates.add(builder.like(root.get("color"), "%" + color + "%"));
            if (status != null) predicates.add(builder.equal(root.get("inventoryStatus"), FactoryEnum.getEnumInstance(EInventoryStatus.class, status)));
            return builder.and(predicates.toArray(new Predicate[0]));
        });
        List<Inventory> inventories = inventoryRepository.findAll(spec);
        log.info("[searchInventory] - Retrieved {} inventories by query dynamic", inventories.size());
        return inventories;
    }

    @Override
    public Inventory create(InventoryRequest request) {
        Products products = productService.getByID(request.getProductID());

        if (request.getStockQuantity() < 0) {
            log.warn("[create] - stockQuantity cannot be less than 0");
            throw new IllegalArgumentException("Invalid stockQuantity: " + request.getStockQuantity());
        }

        Inventory inventory = Inventory.builder()
                .inventoryID(IDGenerate.generate())
                .size(FactoryEnum.getEnumInstance(ESize.class, request.getSize()))
                .color(request.getColor())
                .soldQuantity(0)
                .stockQuantity(request.getStockQuantity())
                .imageUrl(request.getImageUrl())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .inventoryStatus(FactoryEnum.getEnumInstance(EInventoryStatus.class, request.getInventoryStatus()))
                .products(products)
                .build();

        inventoryRepository.save(inventory);
        log.info("[create] - Created successfully");

        return inventory;
    }

    @Override
    public Inventory update(String inventoryID, InventoryRequest request) {
        Inventory oldInventory = getByID(inventoryID);

        if (request.getStockQuantity() < 0) {
            log.warn("[update] - stockQuantity cannot be less than 0");
            throw new IllegalArgumentException("Invalid stockQuantity: " + request.getStockQuantity());
        }

        if (request.getProductID() != null) {
            Products products = productService.getByID(request.getProductID());
            oldInventory.setProducts(products);
        }

        oldInventory.setSize(FactoryEnum.getEnumInstance(ESize.class, request.getSize()));
        oldInventory.setStockQuantity(request.getStockQuantity());
        oldInventory.setImageUrl(request.getImageUrl());
        oldInventory.setUpdatedAt(LocalDateTime.now());
        oldInventory.setInventoryStatus(FactoryEnum.getEnumInstance(EInventoryStatus.class, request.getInventoryStatus()));

        inventoryRepository.save(oldInventory);
        log.info("[update] - Updated successfully");

        return oldInventory;
    }

    @Override
    public void delete(String inventoryID) {
        Inventory inventory = getByID(inventoryID);
        inventoryRepository.delete(inventory);
        log.info("[delete] - Deleted successfully");
    }
}
