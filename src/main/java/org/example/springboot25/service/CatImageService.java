package org.example.springboot25.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class CatImageService {

    private final String apiKey;
    private static final Logger logger = LoggerFactory.getLogger(CatImageService.class);

    @Autowired
    public CatImageService(@Value("${catapi.key:}") String apiKey) {
        if (apiKey == null || apiKey.isBlank()) {
            logger.warn("No CATAPI_KEY provided – fallback image will be used.");
        }
        this.apiKey = apiKey;
    }

    @PostConstruct
    public void logApiKeyPresence() {
        logger.info("🐾 CATAPI_KEY loaded: {}", !apiKey.isBlank());
    }

    public String getCatImageUrl() {
        String apiUrl = "https://api.thecatapi.com/v1/images/search";

        if (apiKey == null || apiKey.isBlank()) {
            return getFallbackImageUrl();
        }

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("x-api-key", apiKey)
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                logger.error("API returned error status: {}", response.statusCode());
                return getFallbackImageUrl();
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.body());

            if (root.isArray() && root.size() > 0 && root.get(0).has("url")) {
                return root.get(0).get("url").asText();
            }
        } catch (Exception e) {
            logger.error("Failed to fetch cat image from API", e);
        }

        return getFallbackImageUrl();
    }

    @Contract(pure = true)
    private @NotNull String getFallbackImageUrl() {
        return "https://cdn2.thecatapi.com/images/MTY3ODIyMQ.jpg";
    }
}

