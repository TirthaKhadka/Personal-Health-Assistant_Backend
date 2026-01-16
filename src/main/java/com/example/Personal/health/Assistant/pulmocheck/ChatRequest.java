package com.example.Personal.health.Assistant.pulmocheck;
import java.util.List;
import java.util.Map;

public class ChatRequest {

    private String message;
    private List<Map<String, String>> history;

    public String getMessage() {
        return message;
    }

    public List<Map<String, String>> getHistory() {
        return history;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setHistory(List<Map<String, String>> history) {
        this.history = history;
    }
}
