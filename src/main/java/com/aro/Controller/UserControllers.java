package com.aro.Controller;

import com.aro.Entity.Users;
import com.aro.Service.JwtService;
import com.aro.Service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
        System.out.println("Hello how are yall doing this and ");
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
        System.out.println("hello this is the login api");

        Authentication authToken = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
        );

        if (authToken.isAuthenticated()) {
            String generatedToken = jwtService.generateTheToken(user);
            System.out.println(generatedToken);
            return ResponseEntity.ok(generatedToken);
        }

        return ResponseEntity.badRequest().body("failed to logged in");
    }

    @GetMapping("/validateToken")
    public ResponseEntity<String> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = jwtService.extractToken(authHeader).getBody(); // now we got the token
            Claims claims = jwtService.extractClaims(token);

            Users user = userService.getUserByEmail(claims.getSubject()); // now we got the
            if (jwtService.isTokenExpired(token)) {
                return ResponseEntity.badRequest().body("token is expired");
            }

            return ResponseEntity.ok("token is validated");
        } catch (ExpiredJwtException e) {
            System.out.println("Token is expired");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired.");
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token.");
        }
    }

}

// so now i have to make the addPasswordTo the Db api return the added password
// now let's try to make the otpSending api