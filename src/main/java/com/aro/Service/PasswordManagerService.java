package com.aro.Service;

import com.aro.DTO.PasswordsDTO;
import com.aro.Entity.Passwords;
import com.aro.Entity.Users;
import com.aro.Repositorys.PasswordsRepo;
import com.aro.Security.AesUtil;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PasswordManagerService {

    private static final Logger log = LoggerFactory.getLogger(PasswordManagerService.class); // this is the Class for logging the errors
    private PasswordsRepo passwordsRepo;
    private JwtService jwtService;
    private UserService userService;
    private AesUtil aesUtil;

    public PasswordManagerService(PasswordsRepo passwordsRepo, JwtService jwtService,
                                  UserService userService, AesUtil aesUtil) {
        this.passwordsRepo = passwordsRepo;
        this.jwtService = jwtService;
        this.userService = userService;
        this.aesUtil = aesUtil;
    }

    private Users extractUser(String authHeader) {
        String token = authHeader.substring(7);

        String userEmail = jwtService.extractSubject(token);
        Users user = userService.getUserByEmail(userEmail);

        return user;
    }

    @Transactional
    public ResponseEntity<String> addNewPassword(String authHeader, Passwords password) {
        Users user = extractUser(authHeader);

        if (user == null) {
            return ResponseEntity.badRequest().body("Failed to add the new password: User not found");
        }

        try {
            // so we are encrypting the password and saving it in the database
            String encryptedPassword = aesUtil.encrypt(password.getPassword());

            Passwords savingPurposePassword = new Passwords(
                user,
                password.getUrl(),
                encryptedPassword
            );

            passwordsRepo.save(savingPurposePassword);

            return ResponseEntity.ok("New password saved successfully");
        } catch (Exception e) {
            log.error("Error while encrypting or saving password: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to add new password due to an internal error");
        }
    }

    @Transactional
    public ResponseEntity<String> removePasswordById(Long passwordId) {
        passwordsRepo.deleteById(passwordId);
        return ResponseEntity.ok("Password entity deleted for the user");
    }

    // now i have to make this api return the user with the all decrypted password
    public ResponseEntity<List<PasswordsDTO>> getAllPasswordsByUserId(String authHeader) {
        Users user = extractUser(authHeader);

        List<Passwords> allPasswords = passwordsRepo.findAllByUser(user);
        List<PasswordsDTO> reqPasswords = new ArrayList<>();
        allPasswords.forEach(password -> {
            try {
                String decryptedPassword = aesUtil.decrypt(password.getPassword());
                reqPasswords.add(
                    new PasswordsDTO(
                        password.getPasswordId(),
                        password.getUrl(),
                        decryptedPassword,
                        password.getCreatedAt()
                    )
                );
            } catch (Exception e) {
                throw new RuntimeException("Error whilte decrypting the password");
            }
        });

        return ResponseEntity.ok(reqPasswords);
    }

    // so this is api is going to be use for the updating the passwords database
    public Passwords updateThePassword(Long passwordId, Passwords update) {
        Passwords passwords = passwordsRepo.findById(passwordId).orElseThrow(() ->
            new RuntimeException("User not found"));

        passwords.setUrl(update.getUrl());
        try {
            String encodedPassword = aesUtil.encrypt(passwords.getPassword());
            passwords.setPassword(encodedPassword);
        } catch (Exception e) {
            log.error("error with encrypting the password");
        }

        return passwordsRepo.save(passwords);
    }

}
