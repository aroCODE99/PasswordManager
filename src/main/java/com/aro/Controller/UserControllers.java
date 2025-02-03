package com.aro.Controller;

import com.aro.Entity.Passwords;
import com.aro.Entity.Users;
import com.aro.Repositorys.PasswordsRepo;
import com.aro.Service.JwtService;
import com.aro.Service.PasswordManagerService;
import com.aro.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserControllers {

    private String DEFUALT_ROLE = "USER";

    private UserService userService;

    private AuthenticationManager authManager;

    private JwtService jwtService;

    private PasswordsRepo passwordsRepo;

    private PasswordManagerService passwordManagerService;

    public UserControllers(UserService userService, AuthenticationManager authManager,
                           JwtService jwtService, PasswordsRepo passwordsRepo, PasswordManagerService passwordManagerService)
    {
        this.userService = userService;
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.passwordsRepo = passwordsRepo;
        this.passwordManagerService = passwordManagerService;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello world!";
    }

    @PostMapping("/register")
    public void register(@RequestBody Users user) {
        userService.addUser(user, DEFUALT_ROLE);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Users user) {
        Authentication authentication = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        if (authentication.isAuthenticated()) {
            String generatedToken = jwtService.generateTheToken(user);
            return ResponseEntity.ok(generatedToken);
        }

        return ResponseEntity.badRequest().body("failed to logged in");
    }

    @PostMapping("/addNewPassword")
    public ResponseEntity<String> addNewPassword(@RequestHeader("Authorization") String authHeader,
                               @RequestBody Passwords passwords)
    {
        return passwordManagerService.addNewPassword(authHeader, passwords);
    }

    @PostMapping("/deleteById/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        return passwordManagerService.removePasswordById(id);
    }

    @PostMapping("/findAllPasswords")
    public ResponseEntity<List<Passwords>> findAllPasswords(@RequestHeader("Authorization") String authHeader) {
        return passwordManagerService.getAllPasswordsByUserId(authHeader);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<Passwords> updateTheGivenPassword(@PathVariable Long id, @RequestBody Passwords updatePassword) {
        Passwords newSavedPassword = passwordManagerService.updateThePassword(id, updatePassword);

        if (newSavedPassword == null) {
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok(newSavedPassword);
    }

}

