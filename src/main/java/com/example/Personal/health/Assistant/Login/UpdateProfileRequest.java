package com.example.Personal.health.Assistant.Login;
import lombok.Data;

@Data
public class UpdateProfileRequest {
    private Long userId;
    private String name;
    private String phonenumber;
}