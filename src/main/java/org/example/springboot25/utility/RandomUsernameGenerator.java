package org.example.springboot25.utility;

import java.util.UUID;

public class RandomUsernameGenerator {
    public static String getRandomUsername() {
        return "CatLover_" +  UUID.randomUUID().toString().substring(0, 8);
    }
}
