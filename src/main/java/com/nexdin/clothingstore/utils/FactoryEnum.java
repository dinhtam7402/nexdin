package com.nexdin.clothingstore.utils;

import com.nexdin.clothingstore.domain.enums.EInventoryStatus;
import com.nexdin.clothingstore.domain.enums.ESize;
import com.nexdin.clothingstore.domain.enums.EVoucherStatus;
import com.nexdin.clothingstore.domain.enums.EVoucherType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FactoryEnum {
    public static EVoucherStatus getInstanceVoucherStatus(String status) {
        if (status == null || status.isEmpty()) {
            log.warn("[getInstanceVoucherStatus] - Voucher status cannot be null or empty");
            throw new IllegalArgumentException("Voucher status cannot be null or empty");
        }

        try {
            EVoucherStatus voucherStatus = EVoucherStatus.valueOf(status.toUpperCase());
            log.info("[getInstanceVoucherStatus] - Convert voucher status from String to Enum success");
            return voucherStatus;
        } catch (IllegalArgumentException exception) {
            log.warn("[getInstanceVoucherStatus] - Invalid voucher status: {}", status);
            throw new IllegalArgumentException("Invalid voucher status: " + status);
        }
    }

    public static EVoucherType getInstanceVoucherType(String type) {
        if (type == null || type.isEmpty()) {
            log.warn("[getInstanceVoucherType] - Voucher type cannot be null or empty");
            throw new IllegalArgumentException("Voucher type cannot be null or empty");
        }

        try {
            EVoucherType voucherType = EVoucherType.valueOf(type.toUpperCase());
            log.info("[getInstanceVoucherType] - Convert voucher type from String to Enum success");
            return voucherType;
        } catch (IllegalArgumentException exception) {
            log.warn("[getInstanceVoucherType] - Invalid voucher type: {}", type);
            throw new IllegalArgumentException("Invalid voucher type: " + type);
        }
    }

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
