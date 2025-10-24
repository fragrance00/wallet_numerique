package com.wallet.backend.entities;

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
    private String email;

    @NotNull
    @Column(unique = true)
    private String username; // identifiant pour la connexion

    @NotNull
    private String passwordHash; // mot de passe hashé


    // Un banquier peut gérer plusieurs comptes
    @OneToMany(mappedBy = "banker", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Account> accounts;
}
