package com.example.Personal.health.Assistant.controller;

import com.example.Personal.health.Assistant.MlPredictionService.MlPredictionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/predict")
@RequiredArgsConstructor
public class PredictionController {

    private final MlPredictionService mlPredictionService;

    @PostMapping
    public ResponseEntity<?> predict(@RequestParam("file") MultipartFile file)
            throws IOException {

        String result = mlPredictionService.predict(file);
        return ResponseEntity.ok(result);
    }
}
