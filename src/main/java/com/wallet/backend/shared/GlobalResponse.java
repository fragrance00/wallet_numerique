package com.wallet.backend.shared;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GlobalResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private String timestamp;

    public GlobalResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = java.time.LocalDateTime.now().toString();
    }

    public GlobalResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.timestamp = java.time.LocalDateTime.now().toString();
    }
}