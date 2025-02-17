package com.nexdin.clothingstore.utils;

import com.nexdin.clothingstore.domain.enums.EInventoryStatus;
import com.nexdin.clothingstore.domain.enums.ESize;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FactoryEnum {
    public static ESize getInstanceSize(String size) {
        if (size == null || size.isEmpty()) {
            log.warn("[getInstanceSize] - Size cannot be null or empty");
            throw new IllegalArgumentException("Size cannot be null or empty");
        }

        try {
            ESize eSize = ESize.valueOf(size.toUpperCase());
            log.info("[getInstanceSize] - Convert size from String to Enum success");
            return eSize;
        } catch (IllegalArgumentException exception) {
            log.warn("[getInstanceSize] - Invalid size: {}", size);
            throw new IllegalArgumentException("Invalid size: " + size);
        }
    }

    public static EInventoryStatus getInstanceInventoryStatus(String status) {
        if (status == null || status.isEmpty()){
            log.warn("[getInstanceInventoryStatus] - Status cannot be null or empty");
            throw new IllegalArgumentException("Status cannot be null or empty");
        }

        try {
            EInventoryStatus inventoryStatus = EInventoryStatus.valueOf(status.toUpperCase());
            log.info("[getInstanceInventoryStatus] - Convert status from String to Enum success");
            return inventoryStatus;
        } catch (IllegalArgumentException exception) {
            log.warn("[getInstanceInventoryStatus] - Invalid status: {}", status);
            throw new IllegalArgumentException("Invalid status: " + status);
        }
    }
}
