package com.project.SupportFlow.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class AIClient {

    private final HttpClient httpClient = HttpClient.newHttpClient();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${ai.apikey}")
    private String apiKey;

    @Value("${ai.apimodel}")
    private String model;

    public String generateResponse(String prompt) throws IOException, InterruptedException {

        String url = "https://generativelanguage.googleapis.com/v1beta/models/"
                + model
                + ":generateContent?key=" + apiKey;

        String jsonBody = """
        {
          "contents": [
            {
              "parts": [
                {
                  "text": "%s"
                }
              ]
            }
          ]
        }
        """.formatted(prompt);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        try {
            HttpResponse<String> response = null;

            for(int attempt = 0; attempt <= 3; attempt++) {
                response = httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    break;
                }

                if (response.statusCode() == 503 && attempt < 3) {
                    Thread.sleep(2000);
                    continue;
                }

                throw new RuntimeException("Failed : API Gemini ERROR : "
                        + response.statusCode() + " - " + response.body());
            }

            JsonNode root = objectMapper.readTree(response.body());

            System.out.println(response.body());
            String text = root
                    .path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();

            return text;
        } catch (IOException |  InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
