package com.example.Personal.health.Assistant.Login;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @Email
    @NotBlank
    @Column(name = "email")  // renamed from UserData to email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String phonenumber; // keep this column as it is
}
