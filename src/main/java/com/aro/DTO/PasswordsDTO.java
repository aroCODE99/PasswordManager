package com.aro.DTO;

import java.time.LocalDateTime;

public class PasswordsDTO {
    private Long passwordId;
    private String url;
    private String decryptedPassword;
    private LocalDateTime createdAt;
    private String note;

    public PasswordsDTO(Long passwordId, String url, String decryptedPassword, LocalDateTime createdAt, String note) {
        this.passwordId = passwordId;
        this.url = url;
        this.decryptedPassword = decryptedPassword;
        this.createdAt = createdAt;
        this.note = note;
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

    public String getNote() {
        return note;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PasswordsDTO{");
        sb.append("passwordId=").append(passwordId);
        sb.append(", url='").append(url).append('\'');
        sb.append(", decryptedPassword='").append(decryptedPassword).append('\'');
        sb.append(", createdAt=").append(createdAt);
        sb.append(", note='").append(note).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
