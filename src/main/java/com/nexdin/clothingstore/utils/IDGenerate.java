package com.nexdin.clothingstore.utils;

import java.util.UUID;

public class IDGenerate {
    public static String generate() {
        return UUID.randomUUID().toString().replace("-", "").substring(0,9);
    }
}
