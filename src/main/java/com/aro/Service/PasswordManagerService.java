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

import java.time.LocalDateTime;
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
    public ResponseEntity<PasswordsDTO> addNewPassword(String authHeader, Passwords password) {
        Users user = extractUser(authHeader);

        if (user == null) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            // so we are encrypting the password and saving it in the database
            String encryptedPassword = aesUtil.encrypt(password.getPassword());

            Passwords savingPurposePassword = new Passwords(
                user,
                password.getUrl(),
                encryptedPassword,
                password.getNote()
            );

            // Save to DB
            Passwords savedEntity = passwordsRepo.save(savingPurposePassword);

            String decryptedPassword = aesUtil.decrypt(savedEntity.getPassword());

            PasswordsDTO savedPassword = new PasswordsDTO(
                savedEntity.getPasswordId(),
                savedEntity.getUrl(),
                decryptedPassword,
                savedEntity.getCreatedAt(),
                savedEntity.getNote()
            );

            System.out.println(savedPassword);
            return ResponseEntity.ok(savedPassword);
        } catch (Exception e) {
            log.error("Error while encrypting or saving password: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(null);
        }
    }

    public ResponseEntity<Long> removePasswordById(Long id) {
        if (passwordsRepo.existsById(id)) {
            passwordsRepo.deleteById(id);
            return ResponseEntity.ok(id);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
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
                        password.getCreatedAt(),
                        password.getNote()
                    )
                );
            } catch (Exception e) {
                throw new RuntimeException("Error whilte decrypting the password");
            }
        });

        return ResponseEntity.ok(reqPasswords);
    }

    // so this is api is going to be use for the updating the passwords database
    public ResponseEntity<PasswordsDTO> updateThePassword(Long passwordId, PasswordsDTO update) {
        Passwords passwords = passwordsRepo.findById(passwordId).orElseThrow(() ->
            new RuntimeException("User not found"));

        passwords.setUrl(update.getUrl()); // url
        passwords.setNote(update.getNote()); // note
        passwords.setCreatedAt(LocalDateTime.now()); // created at / modified at
        try {
            String encodedPassword = aesUtil.encrypt(update.getDecryptedPassword());
            passwords.setPassword(encodedPassword); // password
        } catch (Exception e) {
            log.error("error with encrypting the password");
        }

        passwordsRepo.save(passwords); // now we have to return the id

        PasswordsDTO passwordsDTO = new PasswordsDTO(
            passwords.getPasswordId(),
            passwords.getUrl(),
            update.getDecryptedPassword(),
            passwords.getCreatedAt(),
            passwords.getNote()
        );

        System.out.println(passwordsDTO);

        return ResponseEntity.ok(passwordsDTO);
    }

}
