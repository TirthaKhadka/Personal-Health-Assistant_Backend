package com.example.Personal.health.Assistant.pulmocheck;
import org.springframework.web.bind.annotation.*;

@RestController
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody ChatRequest request) {
        return new ChatResponse(chatService.chat(request));
    }

    @GetMapping("/")
    public String root() {
        return "PulmoCheck AI Backend is running";
    }
}
