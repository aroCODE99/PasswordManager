package com.aro.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "passwords")
public class Passwords {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long passwordId;

    @Column(nullable = false, unique = true)
    private String url; // url in which the user has been logged in

    @Column(nullable = false)
    private String password; // the password for that url

    private LocalDateTime createdAt; // this will store the register time of that password

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    public Passwords() {
    }

    public Passwords(Users user, String url, String password) {
        this.user = user;
        this.url = url;
        this.password = password;
        this.createdAt = LocalDateTime.now();
    }

    public Long getPasswordId() {
        return passwordId;
    }

    public void setPasswordId(Long passwordId) {
        this.passwordId = passwordId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Passwords{");
        sb.append("passwordId=").append(passwordId);
        sb.append(", url='").append(url).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", createdAt=").append(createdAt);
        sb.append(", user=").append(user);
        sb.append('}');
        return sb.toString();
    }
}