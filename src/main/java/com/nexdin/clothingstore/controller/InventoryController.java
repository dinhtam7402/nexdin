package com.nexdin.clothingstore.controller;

import com.nexdin.clothingstore.domain.Inventory;
import com.nexdin.clothingstore.payload.request.InventoryRequest;
import com.nexdin.clothingstore.payload.response.Response;
import com.nexdin.clothingstore.services.IInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class InventoryController {
    @Autowired
    private IInventoryService inventoryService;

    @PostMapping("/admin/inventory/create")
    public ResponseEntity<Response<?>> createInventory(@RequestBody InventoryRequest request) {
        Inventory inventory = inventoryService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                Response.builder().status(HttpStatus.CREATED.value())
                        .message("Created successfully")
                        .timestamp(LocalDateTime.now())
                        .result(inventory)
                        .build()
        );
    }

    @PutMapping("/admin/inventory/update/{inventoryID}")
    public ResponseEntity<Response<?>> updateInventory(@PathVariable String inventoryID, @RequestBody InventoryRequest request) {
        Inventory inventory = inventoryService.update(inventoryID, request);
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder().status(HttpStatus.OK.value())
                        .message("Updated successfully")
                        .timestamp(LocalDateTime.now())
                        .result(inventory)
                        .build()
        );
    }

    @DeleteMapping("/admin/inventory/delete/{inventoryID}")
    public ResponseEntity<Response<?>> deleteInventory(@PathVariable String inventoryID) {
        inventoryService.delete(inventoryID);
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder().status(HttpStatus.OK.value())
                        .message("Deleted successfully")
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping("/inventory/get-by-id/{inventoryID}")
    public ResponseEntity<Response<?>> getByID(@PathVariable String inventoryID){
        Inventory inventory = inventoryService.getByID(inventoryID);
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder().status(HttpStatus.OK.value())
                        .message("success")
                        .timestamp(LocalDateTime.now())
                        .result(inventory)
                        .build()
        );
    }

    @GetMapping("/inventory/get-by-size")
    public ResponseEntity<Response<?>> getBySize(@RequestParam String size) {
        List<Inventory> inventories = inventoryService.getBySize(size);
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder().status(HttpStatus.OK.value())
                        .message("success")
                        .timestamp(LocalDateTime.now())
                        .result(inventories)
                        .build()
        );
    }

    @GetMapping("/inventory/get-by-color")
    public ResponseEntity<Response<?>> getByColor(@RequestParam String color) {
        List<Inventory> inventories = inventoryService.getByColor(color);
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder().status(HttpStatus.OK.value())
                        .message("success")
                        .timestamp(LocalDateTime.now())
                        .result(inventories)
                        .build()
        );
    }

    @GetMapping("/inventory/get-by-inventory-status")
    public ResponseEntity<Response<?>> getByInventoryStatus(@RequestParam String status) {
        List<Inventory> inventories = inventoryService.getByInventoryStatus(status);
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder().status(HttpStatus.OK.value())
                        .message("success")
                        .timestamp(LocalDateTime.now())
                        .result(inventories)
                        .build()
        );
    }

    @GetMapping("/inventory/get-by-product")
    public ResponseEntity<Response<?>> getByProduct(@RequestParam String productID) {
        List<Inventory> inventories = inventoryService.getByProduct(productID);
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder().status(HttpStatus.OK.value())
                        .message("success")
                        .timestamp(LocalDateTime.now())
                        .result(inventories)
                        .build()
        );
    }

    @GetMapping("/inventory/search")
    public ResponseEntity<Response<?>> searchInventory(@RequestParam(required = false) String size,
                                                       @RequestParam(required = false) String color,
                                                       @RequestParam(required = false) String status) {
        List<Inventory> inventories = inventoryService.searchInventory(size, color, status);
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder().status(HttpStatus.OK.value())
                        .message("success")
                        .timestamp(LocalDateTime.now())
                        .result(inventories)
                        .build()
        );
    }

}
