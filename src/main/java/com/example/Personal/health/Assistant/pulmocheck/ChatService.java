package com.example.Personal.health.Assistant.pulmocheck;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
public class ChatService {

    private final WebClient webClient;

    @Value("${openrouter.api.key}")
    private String apiKey;

    @Value("${openrouter.api.url}")
    private String apiUrl;

    @Value("${openrouter.model}")
    private String model;

    public ChatService(WebClient webClient) {
        this.webClient = webClient;
    }

    public String chat(ChatRequest request) {

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", SystemPrompt.PROMPT));

        if (request.getHistory() != null) {
            messages.addAll(request.getHistory());
        }

        messages.add(Map.of("role", "user", "content", request.getMessage()));

        OpenRouterRequest payload = new OpenRouterRequest();
        payload.model = model;
        payload.messages = messages;
        payload.max_tokens = 500;
        payload.temperature = 0.7;

        OpenRouterResponse response = webClient.post()
                .uri(apiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(OpenRouterResponse.class)
                .block();

        if (response == null || response.choices == null || response.choices.isEmpty()) {
            throw new RuntimeException("Invalid response from OpenRouter");
        }

        return response.choices.get(0).message.get("content");
    }
}

