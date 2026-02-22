package com.example.Personal.health.Assistant.Login;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    // ================= REGISTER =================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            String token = authService.register(request);
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response); // returns JSON { "token": "...", "user": {...} }
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }

    // ================= GET LOGGED-IN USER =================
    @GetMapping("/me")
    public ResponseEntity<?> getLoggedInUser(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7); // remove "Bearer "
            String email = jwtUtil.extractEmail(token);

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }
    // ================= UPDATE PROFILE =================
    @PutMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@RequestBody UpdateProfileRequest request) {
        try {

            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            user.setName(request.getName());
            user.setPhonenumber(request.getPhonenumber());

            userRepository.save(user);

            return ResponseEntity.ok(Map.of("message", "Profile updated successfully"));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    // ================= CHANGE PASSWORD =================
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody ChangePasswordRequest request) {
        try {
            // Extract email from JWT
            String token = authHeader.substring(7); // remove "Bearer "
            String email = jwtUtil.extractEmail(token);

            authService.changePassword(email, request);

            return ResponseEntity.ok(Map.of("message", "Password changed successfully"));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }
    }
    @PostMapping("/generate-otp")
    public ResponseEntity<?> generateOtp(@RequestBody GenerateOtpRequest request) {
        try {
            System.out.println("Generating OTP for userId: " + request.getUserId());
            authService.generateOtp(request.getUserId());
            return ResponseEntity.ok().body(Map.of("message", "OTP generated and sent"));

        } catch (Exception e) {
            System.out.println("Generate OTP error: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtpRequest request) {
        try {
            Long userId = request.getUserId();
            String token = authService.verifyOtp(userId, request.getOtp());

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            AuthResponse response = AuthResponse.builder()
                    .token(token)
                    .requires2FA(false)
                    .userId(user.getId())
                    .method("email")  // or phone if using SMS
                    .contact(user.getEmail())
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("Verify OTP error: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Test endpoint for debugging
    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok(Map.of(
                "status", "Server is running",
                "timestamp", System.currentTimeMillis()
        ));
    }

}
