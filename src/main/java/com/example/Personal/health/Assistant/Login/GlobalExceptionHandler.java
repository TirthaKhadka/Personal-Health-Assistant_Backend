package com.example.Personal.health.Assistant.Login;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        String message = ex.getMessage();
        HttpStatus status;

        switch (message) {
            case "Invalid email or password":
                status = HttpStatus.UNAUTHORIZED; // 401
                break;
            case "Invalid or expired OTP":
                status = HttpStatus.BAD_REQUEST; // 400
                break;
            case "Email already exists!":
                status = HttpStatus.CONFLICT; // 409
                break;
            default:
                status = HttpStatus.INTERNAL_SERVER_ERROR; // 500 for unexpected errors
                break;
        }

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", message);

        return new ResponseEntity<>(errorResponse, status);
    }
}
