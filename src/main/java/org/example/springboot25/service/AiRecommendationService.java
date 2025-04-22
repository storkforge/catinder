package org.example.springboot25.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class AiRecommendationService {

    @Value("${openai.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    public String getRecommendationForBreed(String breed) {
        if (breed == null || breed.trim().isEmpty()) {
            return "Unknown breed. Please enter a valid cat breed.";
        }

        breed = breed.trim();

        try {
            // Prepare the OpenAI API request
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            Map<String, Object> message = new HashMap<>();
            message.put("role", "user");
            message.put("content", "Give me a short but useful care tip for the cat breed: " + breed);

            Map<String, Object> body = new HashMap<>();
            body.put("model", "gpt-3.5-turbo");
            body.put("messages", new Object[]{message});
            body.put("max_tokens", 50);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    OPENAI_API_URL,
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> choice = ((java.util.List<Map<String, Object>>) response.getBody().get("choices")).get(0);
                Map<String, Object> messageContent = (Map<String, Object>) choice.get("message");
                return (String) messageContent.get("content");
            } else {
                return "Something went wrong while getting recommendation. Try again later.";
            }

        } catch (Exception e) {
            return "Error calling AI service: " + e.getMessage();
        }
    }
}
