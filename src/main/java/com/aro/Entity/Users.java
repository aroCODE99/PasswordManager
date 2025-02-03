package com.aro.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Version
    private Integer version;  // Optimistic Locking Version Field

    private String email;
    private String password;

    // so i, only want the uni-directional mapping
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<UserRoles> roles;

    // Default constructor
    public Users() {
    }

    // Constructor with fields (excluding version)
    public Users(Long userId, String email, String password) {
        this.email = email;
        this.password = password;
        this.userId = userId;
    }

    // Getter and Setter for version field
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    // Other getters and setters for userId, email, password
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<UserRoles> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRoles> roles) {
        this.roles = roles;
    }
}
