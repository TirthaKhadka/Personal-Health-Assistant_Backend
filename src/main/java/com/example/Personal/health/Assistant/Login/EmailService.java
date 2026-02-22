package com.example.Personal.health.Assistant.Login;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendOtpEmail(String to, String otp) {
        System.out.println("\n=== EMAIL SERVICE ===");
        System.out.println("Sending OTP to: " + to);
        System.out.println("Using sender: puproject3@gmail.com");
        System.out.println("OTP: " + otp);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Your OTP Code - Health AI");
            message.setText(
                    "Your 6-digit OTP code is: " + otp +
                            "\n\nThis code will expire in 5 minutes." +
                            "\n\nIf you didn't request this, please ignore."
            );
            message.setFrom("puproject3@gmail.com");

            System.out.println("Connecting to SMTP server...");
            mailSender.send(message);
            System.out.println("✅ Email sent successfully!");
            System.out.println("Recipient: " + to);

        } catch (Exception e) {
            System.out.println("❌ Email sending failed!");
            System.out.println("Error type: " + e.getClass().getName());
            System.out.println("Error message: " + e.getMessage());
            throw new RuntimeException("Failed to send OTP email: " + e.getMessage(), e);
        }
        System.out.println("====================\n");
    }
}