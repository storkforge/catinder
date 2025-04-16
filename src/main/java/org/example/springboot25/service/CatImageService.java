package org.example.springboot25.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public CatImageService(@Value("${catapi.key:}") String apiKey) {
        this.apiKey = apiKey;
    }

    public String getCatImageUrl() {
        String apiUrl = "https://api.thecatapi.com/v1/images/search";

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
            JsonNode root = mapper.readTree(response.body()); // Cannot resolve method 'readTree(Object)'

            if (root.isArray() && root.size() > 0) {
                return root.get(0).get("url").asText();
            }
        } catch (Exception e) {
            logger.error("Failed to fetch cat image from API", e);
        }
        return getFallbackImageUrl();
    }

    private String getFallbackImageUrl() {
        return "https://cdn2.thecatapi.com/images/MTY3ODIyMQ.jpg";
    }

}
