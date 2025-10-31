package com.wallet.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// AuthResponse.java
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String token;
    private String userType;
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String message;
}
