package org.example.springboot25.service;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class AboutService {

    // Optionally, you can inject the file path via a property
    @Value("${app.readme.path:README.md}")
    private String readmeFilePath;

    // Create and reuse the parser and renderer beans (they are thread safe)
    private final Parser parser;
    private final HtmlRenderer renderer;

    public AboutService() {
        this.parser = Parser.builder().build();
        this.renderer = HtmlRenderer.builder().build();
    }

    /**
     * Reads a markdown file and converts it to HTML.
     *
     * @return the rendered HTML content
     * @throws IOException if file reading fails
     */
    public String renderMarkdownFile() throws IOException {
        Path path = Paths.get(readmeFilePath);
        String markdown = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        Node document = parser.parse(markdown);
        return renderer.render(document);
    }
}
