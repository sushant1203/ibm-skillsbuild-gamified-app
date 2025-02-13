package com.example.skillsync.utils;

import java.util.regex.Pattern;

public class PasswordValidator {
    // Password must contain at least 8 characters, one uppercase letter, one digit, and one special character.
    private static final String PASSWORD_PATTERN =
            "^(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=.*\\d).{8,}$";

    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    public static boolean isValid(String password) {
        return password != null && pattern.matcher(password).matches();
    }
}
