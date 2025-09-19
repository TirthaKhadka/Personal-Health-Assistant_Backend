package com.example.Personal.health.Assistant.Login;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

// Make sure you have a User entity in the same package or imported
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Custom method to find user by email for login
    Optional<User> findByEmail(String email);
}
