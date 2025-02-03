package com.aro.Service;

import com.aro.Entity.Passwords;
import com.aro.Entity.Users;
import com.aro.Repositorys.PasswordsRepo;
import io.jsonwebtoken.Claims;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PasswordManagerService {

    private PasswordsRepo passwordsRepo;
    private JwtService jwtService;
    private UserService userService;
    private PasswordEncoder passwordEncoder;

    public PasswordManagerService(PasswordsRepo passwordsRepo, JwtService jwtService,
                                  UserService userService, PasswordEncoder passwordEncoder) {
        this.passwordsRepo = passwordsRepo;
        this.jwtService = jwtService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    private String encodePassword(String newPassword) {
        return passwordEncoder.encode(newPassword);
    }
    private Users extractUser(String authHeader) {
        String token = authHeader.substring(7);

        Claims allClaims = jwtService.extractClaims(token);
        String userEmail = (String) allClaims.get("userEmail");
        Users user = userService.getUserByEmail(userEmail);

        return user;
    }

    public ResponseEntity<String> addNewPassword(String authHeader, Passwords passwords) {
        Users user = extractUser(authHeader);

        if (user != null) {
            // Now the user is attached to the session and can be safely used
            String encodedPassword = encodePassword(passwords.getPassword());
            Passwords savingPurposePassword = new Passwords(
                user,  // Properly initialized user entity
                passwords.getUrl(),
                encodedPassword
            );

            passwordsRepo.save(savingPurposePassword);
            return ResponseEntity.ok("new Password saved successfully");
        } else {
            return ResponseEntity.badRequest().body("Unable to find the User");
        }
    }

    @Transactional
    public ResponseEntity<String> removePasswordById(Long passwordId) {
        passwordsRepo.deleteById(passwordId);
        return ResponseEntity.ok("Password entity deleted for the user");
    }

    public ResponseEntity<List<Passwords>> getAllPasswordsByUserId(String authHeader) {
        Users user = extractUser(authHeader);

        List<Passwords> allPasswords = passwordsRepo.findAllByUser(user);

        if (allPasswords.isEmpty()) {
            return ResponseEntity.badRequest().body(new ArrayList<>());
        }

        return ResponseEntity.ok(allPasswords);
    }

    // so this is api is going to be use for the updating the passwords database
    public Passwords updateThePassword(Long passwordId, Passwords update) {
        Passwords passwords = passwordsRepo.findById(passwordId).orElseThrow(() ->
            new RuntimeException("User not found"));

        passwords.setUrl(update.getUrl());
        String encodedPassword = encodePassword(passwords.getPassword());
        passwords.setPassword(encodedPassword);

        return passwordsRepo.save(passwords);
    }
}
