package com.aro.Controller;

import com.aro.DTO.PasswordsDTO;
import com.aro.Entity.Passwords;
import com.aro.Entity.Users;
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

    public UserControllers(UserService userService, AuthenticationManager authManager,
                           JwtService jwtService) {
        this.userService = userService;
        this.authManager = authManager;
        this.jwtService = jwtService;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello world!";
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Users user) {
        try {
            userService.addUser(user, DEFUALT_ROLE);
            return ResponseEntity.ok("user added successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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

}

// so now i have to make the addPasswordTo the Db api return the added password
// now let's try to make the otpSending api