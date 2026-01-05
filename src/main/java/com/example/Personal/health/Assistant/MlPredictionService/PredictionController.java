package com.example.Personal.health.Assistant.MlPredictionService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.Personal.health.Assistant.Login.User;
import com.example.Personal.health.Assistant.Login.UserRepository;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PredictionController {

    private final MlPredictionService mlPredictionService;
    private final UserRepository userRepository; // make sure you have this

    @PostMapping("/predict")
    public ResponseEntity<?> predictPneumonia(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") Long userId
    ) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String mlResponse = mlPredictionService.predict(file, user);

            return ResponseEntity.ok(mlResponse);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}
