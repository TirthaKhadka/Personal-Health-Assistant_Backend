package com.example.Personal.health.Assistant.pulmocheck;
import java.util.List;
import java.util.Map;

public class OpenRouterRequest {
    public String model;
    public List<Map<String, String>> messages;
    public int max_tokens;
    public double temperature;
}
