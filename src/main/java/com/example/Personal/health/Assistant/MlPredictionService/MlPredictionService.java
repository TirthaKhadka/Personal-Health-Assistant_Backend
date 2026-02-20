package com.example.Personal.health.Assistant.MlPredictionService;

import com.example.Personal.health.Assistant.Login.User;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MlPredictionService {

    private static final String ML_API_URL = "http://0.0.0.0:8000/predict";
    private static final String ML_TB_API_URL = "http://0.0.0.0:8000/predict-tb";
    private static final String API_KEY = "SPRING_TO_FASTAPI_SECRET";

    private final RestTemplate restTemplate = new RestTemplate();
    private final PredictionRepository predictionRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public MlPredictionService(PredictionRepository predictionRepository) {
        this.predictionRepository = predictionRepository;
    }

    /* ===================== PREDICT ===================== */
    public String predict(MultipartFile file, User user) throws IOException {

        // --- Prepare headers and body ---
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("X-API-KEY", API_KEY);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        });

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

        // --- Call FastAPI ---
        ResponseEntity<String> response = restTemplate.postForEntity(ML_API_URL, request, String.class);

        // --- Parse response ---
        Map<String, Object> resultMap = objectMapper.readValue(response.getBody(), Map.class);
        String diagnosis = (String) resultMap.get("diagnosis");
        double confidence = ((Number) resultMap.get("probability")).doubleValue();
        String lungOpacity = (String) resultMap.get("lung_opacity");

        // --- Save prediction to DB ---
        Prediction prediction = Prediction.builder()
                .user(user)
                .diseaseName("Pneumonia")
                .predictionResult(diagnosis)
                .confidenceScore(confidence)
                .lungOpacity(lungOpacity)
                .imagePath(null) // optional
                .predictedAt(LocalDateTime.now())
                .build();

        predictionRepository.save(prediction);

        return response.getBody();
    }

    // ===================== NEW: PREDICT TUBERCULOSIS =====================
    public String predictTB(MultipartFile file, User user) throws IOException {

        // --- Prepare headers and body ---
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("X-API-KEY", API_KEY);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        });

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

        // --- Call FastAPI TB endpoint ---
        ResponseEntity<String> response = restTemplate.postForEntity(ML_TB_API_URL, request, String.class);

        // --- Parse response ---
        Map<String, Object> resultMap = objectMapper.readValue(response.getBody(), Map.class);
        String diagnosis = (String) resultMap.get("diagnosis");
        double confidence = ((Number) resultMap.get("probability")).doubleValue();
        String lungOpacity = (String) resultMap.get("lung_opacity");

        // --- Save TB prediction to DB ---
        Prediction prediction = Prediction.builder()
                .user(user)
                .diseaseName("Tuberculosis") // <--- Important difference
                .predictionResult(diagnosis)
                .confidenceScore(confidence)
                .lungOpacity(lungOpacity)
                .imagePath(null) // optional
                .predictedAt(LocalDateTime.now())
                .build();

        predictionRepository.save(prediction);

        return response.getBody();
    }


    /* ===================== GET USER HISTORY ===================== */
    public List<Prediction> getUserHistory(User user) {
        return predictionRepository.findByUserOrderByPredictedAtDesc(user);
    }
}
