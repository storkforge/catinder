package org.example.springboot25.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Controller
public class LanguageController {
    private static final Logger logger = LoggerFactory.getLogger(LanguageController.class);
    private static final List<String> SUPPORTED_LANGUAGES = Arrays.asList("en", "sv", "fr");


    private final LocaleResolver localeResolver;

    @Autowired
    public LanguageController(LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
    }

    @GetMapping("/change-language")
    public String changeLanguage(@RequestParam("lang") String lang,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        logger.info("Attempting to switch locale to: {}", lang);

        if (SUPPORTED_LANGUAGES.contains(lang)) {
            Locale newLocale = new Locale(lang);
            localeResolver.setLocale(request, response, newLocale);
            logger.info("Successfully switched locale to: {}", lang);
        } else {
            logger.warn("Attempted to switch to unsupported language: {}", lang);
        }

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }
}