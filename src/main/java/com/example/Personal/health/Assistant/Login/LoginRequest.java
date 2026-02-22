package com.example.Personal.health.Assistant.Login;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
    private Boolean biometricLogin; // Add this field

    // Add this getter method
    public Boolean getBiometricLogin() {
        return biometricLogin;
    }

    // Helper method to check if biometric login
    public boolean isBiometricLogin() {
        return biometricLogin != null && biometricLogin;
    }
}
