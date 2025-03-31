package org.example.springboot25.validation;

import org.example.springboot25.exception.BadRequestException;
import org.example.springboot25.exception.ConflictException;
import org.example.springboot25.exception.NotFoundException;

import java.util.Optional;

public class ServiceValidator {

    // 💡 Generell null-koll för bad request
    public static void requireNotNull(Object value, String message) {
        if (value == null) {
            throw new BadRequestException(message);
        }
    }

    // 💡 Kasta om något inte hittas (t.ex. Optional tom)
    public static <T> T requireFound(Optional<T> optional, String message) {
        return optional.orElseThrow(() -> new NotFoundException(message));
    }

    // 💡 Kasta vid logisk konflikt, t.ex. redan anmäld
    public static void requireNotConflict(boolean condition, String message) {
        if (condition) {
            throw new ConflictException(message);
        }
    }
}

