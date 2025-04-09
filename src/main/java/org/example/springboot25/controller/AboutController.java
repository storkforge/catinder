package org.example.springboot25.controller;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class AboutController {

    @GetMapping("/about")
    public String aboutPage(Model model) {
        try {
            Path readmePath = Paths.get("README.md");
            String markdown = new String(Files.readAllBytes(readmePath), StandardCharsets.UTF_8);

            Parser parser = Parser.builder().build();
            Node document = parser.parse(markdown);
            HtmlRenderer renderer = HtmlRenderer.builder().build();
            String htmlContent = renderer.render(document);

            model.addAttribute("readmeContent", htmlContent);
        } catch (IOException e) {
            model.addAttribute("readmeContent", "Unable to load README file.");
        }

        return "about";
    }
}
