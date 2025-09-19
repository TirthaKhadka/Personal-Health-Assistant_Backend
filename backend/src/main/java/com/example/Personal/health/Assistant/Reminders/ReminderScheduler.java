package com.example.Personal.health.Assistant.Reminders;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class ReminderScheduler {

    private final ReminderRepository repo;
    private final NotificationService notificationService;

    public ReminderScheduler(ReminderRepository repo, NotificationService notificationService) {
        this.repo = repo;
        this.notificationService = notificationService;
    }

    // Run every minute
    @Scheduled(fixedRate = 60000)
    public void checkReminders() {
        LocalDateTime now = LocalDateTime.now();
        List<Reminder> dueReminders = repo.findByRemindAtBeforeAndCompletedFalse(now);

        for (Reminder r : dueReminders) {
            notificationService.sendNotification(
                    r.getUserDeviceToken(),
                    "Reminder: " + r.getTitle(),
                    r.getDescription() != null ? r.getDescription() : "It's time!"
            );
            r.setCompleted(true);
            repo.save(r);
        }
    }
}