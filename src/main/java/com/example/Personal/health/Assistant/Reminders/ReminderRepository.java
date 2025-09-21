package com.example.Personal.health.Assistant.Reminders;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    List<Reminder> findByRemindAtBeforeAndCompletedFalse(LocalDateTime now);
}
