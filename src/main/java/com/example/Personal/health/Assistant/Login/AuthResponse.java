package com.example.Personal.health.Assistant.Login;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse {
    private String token;

    @JsonProperty("requires_2fa")
    private boolean requires2FA;

    @JsonProperty("user_id")
    private Long userId;

    private String method;
    private String contact;

    // Constructor for just token
    public AuthResponse(String token) {
        this.token = token;
    }

    // Constructor for full 2FA response
    public AuthResponse(String token, boolean requires2FA, Long userId, String method, String contact) {
        this.token = token;
        this.requires2FA = requires2FA;
        this.userId = userId;
        this.method = method;
        this.contact = contact;
    }
}