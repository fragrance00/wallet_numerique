package com.wallet.backend.shared.Exceptions;

import com.wallet.backend.shared.GlobalResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class GlobalSuccessHandler {

    public static <T> ResponseEntity<GlobalResponse<T>> success(String message, T data) {
        GlobalResponse<T> response = new GlobalResponse<>(true, message, data);
        return ResponseEntity.ok(response);
    }

    public static <T> ResponseEntity<GlobalResponse<T>> created(String message, T data) {
        GlobalResponse<T> response = new GlobalResponse<>(true, message, data);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public static ResponseEntity<GlobalResponse<String>> deleted(String message) {
        GlobalResponse<String> response = new GlobalResponse<>(true, message, null);
        return ResponseEntity.ok(response);
    }

    public static <T> ResponseEntity<GlobalResponse<T>> updated(String message, T data) {
        GlobalResponse<T> response = new GlobalResponse<>(true, message, data);
        return ResponseEntity.ok(response);
    }

    public static ResponseEntity<GlobalResponse<String>> noContent(String message) {
        GlobalResponse<String> response = new GlobalResponse<>(true, message, null);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}