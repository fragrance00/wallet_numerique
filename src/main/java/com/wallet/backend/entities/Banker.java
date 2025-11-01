package com.wallet.backend.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "bankers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Banker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @Email
    @Column(unique = true)
    private String email;

    @NotNull
    @Column(unique = true)
    private String username;

    // ⚠️ CHANGEMENT : passwordHash → password (stockage en clair)
    @NotNull
    private String password; // Maintenant en clair

    @OneToMany(mappedBy = "banker", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Account> accounts;

    // 👑 Champ pour distinguer les rôles parmi les bankers
    private String role = "BANKER"; // Peut être "SUPER_ADMIN" ou "BANKER"
}