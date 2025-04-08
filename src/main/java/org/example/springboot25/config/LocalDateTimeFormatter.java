package org.example.springboot25.config;

import org.springframework.format.Formatter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocalDateTimeFormatter implements Formatter<LocalDateTime> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Override
    public LocalDateTime parse(String text, Locale locale) {
        return LocalDateTime.parse(text, FORMATTER);
    }

    @Override
    public String print(LocalDateTime object, Locale locale) {
        return object.format(FORMATTER);
    }
}
