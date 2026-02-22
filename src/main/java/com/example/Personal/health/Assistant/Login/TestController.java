package com.example.Personal.health.Assistant.Login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private JavaMailSender mailSender;

    @GetMapping("/test-email")
    public String testEmail() {
        try {
            System.out.println("\n=== TEST EMAIL CONFIGURATION ===");
            System.out.println("Testing SMTP connection...");

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("thesushilgiri0987@gmail.com");
            message.setSubject("Test Email - Health AI");
            message.setText("If you receive this, email configuration is working correctly!");
            message.setFrom("puproject3@gmail.com");

            mailSender.send(message);
            System.out.println("✅ Test email sent successfully!");
            System.out.println("Check your inbox for the test email.");
            System.out.println("===============================\n");

            return "✅ Email test passed! Check your inbox.";

        } catch (Exception e) {
            System.out.println("❌ Test email failed!");
            System.out.println("Error: " + e.getClass().getName());
            System.out.println("Message: " + e.getMessage());
            e.printStackTrace();
            System.out.println("===============================\n");
            return "❌ Email test failed: " + e.getMessage();
        }
    }
}
