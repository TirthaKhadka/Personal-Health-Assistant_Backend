package com.example.Personal.health.Assistant.Login;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // ================= REGISTER =================
    public String register(RegisterRequest request) {
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

        // Return JWT token after registration (optional)
        return jwtUtil.generateToken(user.getEmail());
    }

    // ================= LOGIN =================
    public UserTokenResponse login(LoginRequest request) {
        // 1️⃣ Find user
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2️⃣ Check password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // 3️⃣ Generate JWT token
        String token = jwtUtil.generateToken(user.getEmail());

        // 4️⃣ Return token + user
        return new UserTokenResponse(token, user);
    }
}
