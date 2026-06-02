package com.gmh.cricket_app.util;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class PasswordUtil {

    public static String hashPassword(String plain) {
        return BCrypt.hashpw(plain, BCrypt.gensalt());
    }

    public static boolean verify(String plain, String hash) {
        return BCrypt.checkpw(plain, hash);
    }
}

