package com.example.Personal.health.Assistant.Login;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserTokenResponse {
    private String token;
    private User user;
}
