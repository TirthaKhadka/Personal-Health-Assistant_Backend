package com.example.Personal.health.Assistant.Reminders;

import com.google.firebase.messaging.*;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public void sendNotification(String deviceToken, String title, String body) {
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        Message message = Message.builder()
                .setToken(deviceToken)
                .setNotification(notification)
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Notification sent: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
