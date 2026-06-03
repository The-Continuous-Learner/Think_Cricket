package com.gmh.cricket_app.util;

import java.util.UUID;

public class CommonUtil {
    public static String generateId(int idLength) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return uuid.substring(0, Math.min(idLength, uuid.length()));
    }
}
