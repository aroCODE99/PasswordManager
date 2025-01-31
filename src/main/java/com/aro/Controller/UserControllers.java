package com.aro.Controller;


import com.aro.Entity.User;
import com.aro.Service.JwtService;
import com.aro.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserControllers {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/hello")
    public String hello() {
        return "Hello world!";
    }

    @PostMapping("/register")
    public void register(@RequestBody User user) {
        userService.addUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        Authentication authentication = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        if (authentication.isAuthenticated()) {
            return ResponseEntity.ok(jwtService.generateTheToken(user.getEmail()));
        }

        return ResponseEntity.badRequest().body("failed to logged in");
    }
}