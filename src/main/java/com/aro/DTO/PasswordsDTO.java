package com.aro.DTO;

import java.time.LocalDateTime;

public class PasswordsDTO {
    private Long passwordId;
    private String url;
    private String decryptedPassword;
    private LocalDateTime createdAt;

    public PasswordsDTO(Long passwordId, String url, String decryptedPassword, LocalDateTime createdAt) {
        this.passwordId = passwordId;
        this.url = url;
        this.decryptedPassword = decryptedPassword;
        this.createdAt = createdAt;
    }

    public Long getPasswordId() {
        return passwordId;
    }

    public String getUrl() {
        return url;
    }

    public String getDecryptedPassword() {
        return decryptedPassword;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
