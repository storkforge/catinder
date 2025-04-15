package org.example.springboot25.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UsernameValidator {
          /**
          * Username pattern requirements:
          * - 5-20 characters total
          * - Must start and end with alphanumeric characters
          * - Can contain dots, underscores, or hyphens in the middle
          * - No consecutive dots, underscores, or hyphens are allowed
          */
    private static final String USERNAME_PATTERN =
            "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$";

    private static final Pattern pattern = Pattern.compile(USERNAME_PATTERN);

    public static boolean isValid(final String username) {
        if (username == null || username.isEmpty()) {
            return false;
        }
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }
}
