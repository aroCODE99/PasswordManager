package com.aro.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class OTP {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(nullable = false)
    private String otp;

    private LocalDateTime expiresAt;

    private LocalDateTime createdAt;

    public OTP() {
    }

    public OTP(Users user, String otp) {
        this.user = user;
        this.otp = otp;
        this.createdAt = LocalDateTime.now();
        this.expiresAt = createdAt.plusMinutes(5);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public LocalDateTime getExpiryTime() {
        return expiresAt;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OTP{");
        sb.append("id=").append(id);
        sb.append(", user=").append(user);
        sb.append(", otp='").append(otp).append('\'');
        sb.append(", expiryTime=").append(expiresAt);
        sb.append(", createdDate=").append(createdAt);
        sb.append('}');
        return sb.toString();
    }
}


// now let's see the working of this and how will we be able to do anything
// first of all we need this entity to save the otp in the db it should be linked with the users(user_id);
// users -> many otp (with the expiry time)
// so we are storing the otp with the user_id correct


// first name then data_type and then any constraint
// BIGINT because we are using the Long
// Useful for expiration logic → You can compare created_date with NOW() in SQL queries to check if the OTP is expired.
// CREATE TABLE otp (
//     id BIGINT AUTO_INCREMENT PRIMARY KEY,
//     user_id BIGINT NOT NULL,
//     otp VARCHAR(6) NOT NULL,
//     expires_at DATETIME NOT NULL,
//     created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
//     FORIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
// );