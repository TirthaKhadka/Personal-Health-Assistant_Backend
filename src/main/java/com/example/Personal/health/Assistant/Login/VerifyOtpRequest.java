package com.example.Personal.health.Assistant.Login;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VerifyOtpRequest {
    @JsonProperty("user_id")
    private Long userId;
    private String otp;
}
