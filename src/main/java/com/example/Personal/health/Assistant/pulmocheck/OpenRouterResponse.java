package com.example.Personal.health.Assistant.pulmocheck;
import java.util.List;
import java.util.Map;

public class OpenRouterResponse {

    public List<Choice> choices;

    public static class Choice {
        public Map<String, String> message;
    }
}
