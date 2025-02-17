package com.nexdin.clothingstore.utils;

import com.nexdin.clothingstore.domain.enums.EInventoryStatus;
import com.nexdin.clothingstore.domain.enums.ESize;
import com.nexdin.clothingstore.domain.enums.EVoucherStatus;
import com.nexdin.clothingstore.domain.enums.EVoucherType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FactoryEnum {
    public static <T extends Enum<T>> T getEnumInstance(Class<T> enumclass, String value) {
        if (value == null || value.isEmpty()) {
            log.warn("[getEnumInstance] - {} cannot be null or empty", enumclass.getSimpleName());
            throw new IllegalArgumentException(enumclass.getSimpleName() + " cannot be null or empty");
        }

        T enumValue = null;
        try {
            enumValue = Enum.valueOf(enumclass, value.toUpperCase());
            log.info("[getEnumInstance] - Converted {} from String to Enum successfully", enumclass.getSimpleName());
            return enumValue;
        } catch (IllegalArgumentException exception) {
            log.warn("[getEnumInstance] - Invalid {}: {}", enumclass.getSimpleName(), enumValue);
            throw new IllegalArgumentException("Invalid " + enumclass.getSimpleName() + ": " + enumValue);
        }
    }
}
