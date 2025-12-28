package com.example.Personal.health.Assistant.MlPredictionService;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class MlPredictionService {

    private static final String ML_API_URL = "http://localhost:8000/predict";
    private static final String API_KEY = "SPRING_TO_FASTAPI_SECRET";

    private final RestTemplate restTemplate = new RestTemplate();

    public String predict(MultipartFile file) throws IOException {

        // Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("X-API-KEY", API_KEY);

        // Body
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        });

        // Request
        HttpEntity<MultiValueMap<String, Object>> request =
                new HttpEntity<>(body, headers);

        // Call FastAPI
        ResponseEntity<String> response =
                restTemplate.postForEntity(
                        ML_API_URL,
                        request,
                        String.class
                );

        return response.getBody();
    }
}
