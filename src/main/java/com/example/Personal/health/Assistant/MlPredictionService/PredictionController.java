package com.example.Personal.health.Assistant.MlPredictionService;

import lombok.RequiredArgsConstructor;
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
    private final UserRepository userRepository;

    /* ===================== PREDICT ===================== */

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
            return ResponseEntity.status(500)
                    .body("Prediction Error: " + e.getMessage());
        }
    }
    /* ===================== PREDICT TUBERCULOSIS ===================== */
    @PostMapping("/predict-tb")
    public ResponseEntity<?> predictTuberculosis(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") Long userId
    ) {
        try {
            // Fetch the user
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Call TB prediction service
            String mlResponse = mlPredictionService.predictTB(file, user);

            return ResponseEntity.ok(mlResponse);

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("TB Prediction Error: " + e.getMessage());
        }
    }


    /* ===================== GET HISTORY ===================== */

    @GetMapping("/history/{userId}")
    public ResponseEntity<?> getUserPredictionHistory(
            @PathVariable Long userId
    ) {
        try {

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            List<Prediction> history =
                    mlPredictionService.getUserHistory(user);

            return ResponseEntity.ok(history);

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("History Fetch Error: " + e.getMessage());
        }
    }
}
