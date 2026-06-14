package com.spkbansos.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
    
    // Generate BCrypt hash for a plain text password
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }
    
    // Verify a plain text password against a hashed one
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        if (hashedPassword == null || !hashedPassword.startsWith("$2a$")) {
            return false; // not a valid bcrypt hash format
        }
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
