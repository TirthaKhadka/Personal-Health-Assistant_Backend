package com.example.Personal.health.Assistant.MlPredictionService;

import com.example.Personal.health.Assistant.Login.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PredictionRepository extends JpaRepository<Prediction, Long> {
    List<Prediction> findByUserOrderByPredictedAtDesc(User user);
}
