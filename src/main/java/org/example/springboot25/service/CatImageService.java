package org.example.springboot25.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class CatImageService {

    public String getCatImageUrl() {
        String apiUrl = "https://api.thecatapi.com/v1/images/search";

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.body());

            if(root.isArray() && root.size() > 0) {
                return root.get(0).get("url").asText();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "https://cdn2.thecatapi.com/images/MTY3ODIyMQ.jpg";
    }
}
