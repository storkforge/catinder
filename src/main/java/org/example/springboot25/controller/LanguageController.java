package org.example.springboot25.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Locale;

@Controller
public class LanguageController {

    @GetMapping("/change_language")
    public String changeLanguage(@RequestParam ("lang") String lang,
                                 HttpServletResponse request) {

        LocaleContextHolder.setLocale(new Locale(lang));

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/"); //för att komma till hemsidan som man va på
//        return "redirect:/"; //Redirect vart?
    }

}
