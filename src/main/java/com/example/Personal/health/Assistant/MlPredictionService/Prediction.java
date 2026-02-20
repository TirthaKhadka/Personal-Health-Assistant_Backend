package com.example.Personal.health.Assistant.MlPredictionService;

import com.example.Personal.health.Assistant.Login.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "prediction_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prediction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(name = "disease_name")
    private String diseaseName;              // default 'Pneumonia'

    @Column(name = "prediction_result")
    private String predictionResult;         // "Pneumonia" / "No Pneumonia"

    @Column(name = "confidence_score")
    private double confidenceScore;          // float

    @Column(name = "lung_opacity")
    private String lungOpacity;              // "Present" / "Absent"

    @Column(name = "image_path")
    private String imagePath;                // optional, can be null

    @Column(name = "predicted_at")
    private LocalDateTime predictedAt;       // timestamp
}
