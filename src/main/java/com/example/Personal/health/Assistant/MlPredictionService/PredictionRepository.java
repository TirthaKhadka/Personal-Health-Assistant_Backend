package com.example.Personal.health.Assistant.MlPredictionService;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PredictionRepository extends JpaRepository<Prediction, Long> {
    // custom queries if needed
}
