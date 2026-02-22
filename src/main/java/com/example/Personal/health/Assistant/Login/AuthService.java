package com.example.Personal.health.Assistant.Login;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final OtpRepository otpRepository;          // NEW
    private final EmailService emailService;           // NEW
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // ================= REGISTER =================
    public String register(RegisterRequest request) {
        System.out.println("=== REGISTER DEBUG ===");
        System.out.println("Registering: " + request.getEmail());

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists!");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phonenumber(request.getPhonenumber())
                .build();

        userRepository.save(user);
        System.out.println("User registered successfully: " + user.getEmail());

        // Return JWT token after registration
        return jwtUtil.generateToken(user.getEmail());
    }

    // ================= LOGIN =================
    public AuthResponse login(LoginRequest request) {
        System.out.println("\n=== LOGIN DEBUG ===");
        System.out.println("Login attempt for: " + request.getEmail());
        System.out.println("Biometric login: " + request.getBiometricLogin());

        // Find user
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    System.out.println("❌ User not found: " + request.getEmail());
                    return new RuntimeException("Invalid email or password");
                });

        System.out.println("User found: " + user.getEmail());
        System.out.println("Checking password...");

        // Check password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            System.out.println("❌ Password mismatch");
            throw new RuntimeException("Invalid email or password");
        }

        System.out.println("✅ Password verified");

        // Biometric login bypasses 2FA
        Boolean biometricLoginFlag = request.getBiometricLogin();
        boolean isBiometricLogin = biometricLoginFlag != null && biometricLoginFlag;

        if (isBiometricLogin) {
            System.out.println("🔐 Biometric login detected - bypassing 2FA");

            String token = jwtUtil.generateToken(user.getEmail());
            System.out.println("✅ Token generated for biometric login");

            return AuthResponse.builder()
                    .token(token)
                    .requires2FA(false)
                    .userId(user.getId())
                    .build();
        } else {
            // Regular login - require 2FA
            System.out.println("📧 Regular login - 2FA required");

            String otp = generateOtp();
            saveOtp(user.getId(), otp);

            try {
                emailService.sendOtpEmail(user.getEmail(), otp);
                System.out.println("✅ OTP email sent successfully");
            } catch (Exception e) {
                System.out.println("❌ Email sending failed! Fallback OTP: " + otp);
            }

            return AuthResponse.builder()
                    .requires2FA(true)
                    .userId(user.getId())
                    .method("email")
                    .contact(user.getEmail())
                    .build();
        }
    }

    // ================= GENERATE OTP =================
    @Transactional
    public void generateOtp(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String otp = generateOtp();
        saveOtp(userId, otp);

        try {
            emailService.sendOtpEmail(user.getEmail(), otp);
        } catch (Exception e) {
            System.out.println("❌ Failed to send OTP email. OTP: " + otp);
        }
    }

    // ================= VERIFY OTP =================
    @Transactional
    public String verifyOtp(Long userId, String otpCode) {
        LocalDateTime now = LocalDateTime.now();
        Otp otp = otpRepository.findByUserIdAndOtpCodeAndExpiresAtAfter(userId, otpCode, now)
                .orElseThrow(() -> new RuntimeException("Invalid or expired OTP"));

        // Delete OTP after use
        otpRepository.delete(otp);
        otpRepository.deleteExpiredOtps(now);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return jwtUtil.generateToken(user.getEmail());
    }

    // ================= UPDATE PROFILE =================
    public void updateProfile(UpdateProfileRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(request.getName());
        user.setPhonenumber(request.getPhonenumber());

        userRepository.save(user);
    }

    // ================= CHANGE PASSWORD =================
    public void changePassword(String email, ChangePasswordRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("New password and confirm password do not match");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    // ================= HELPER METHODS =================
    private String generateOtp() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000); // 6-digit OTP
        return String.valueOf(otp);
    }

    private void saveOtp(Long userId, String otp) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusMinutes(5);

        Otp otpEntity = Otp.builder()
                .userId(userId)
                .otpCode(otp)
                .createdAt(now)
                .expiresAt(expiresAt)
                .build();

        otpRepository.save(otpEntity);
    }
}