package com.aro.Service;

import com.aro.Entity.Users;
import com.aro.Repositorys.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtService {

    @Autowired
    private UserRepository userRepo;

    @Value("${jwt.secret}")
    private String secretKey;

    // now we also have to change this method to also include the validation of the 2fa
    public String generateTheToken(Users user) {
        long expirationTime = 60 * 60 * 1000; // 30 minutes in milliseconds
        Map<String, Object> claims = new HashMap<>();

        // fetching the whole user from the database because we need the userId for other purposes
        Users fetchedUser = userRepo.findByEmail(user.getEmail()).orElseThrow(
            () -> new UsernameNotFoundException("Not Found"));
        claims.put("userId", fetchedUser.getUserId());  // Ensure userId is explicitly included

        return Jwts.builder()
            .claims(claims)
            .subject(user.getEmail())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + expirationTime))
            .signWith(getKey()) // Sign the token
            .compact();
    }

    public String generateTheToken(Users user, boolean is2FAValidated) {
        long expirationTime = 30 * 60 * 1000; // 30 minutes in milliseconds
        Map<String, Object> claims = new HashMap<>();

        // fetching the whole user from the database because we need the userId for other purposes
        Users fetchedUser = userRepo.findByEmail(user.getEmail()).orElseThrow(
            () -> new UsernameNotFoundException("Not Found"));
        claims.put("userId", fetchedUser.getUserId());  // Ensure userId is explicitly included
        claims.put("IS_2FA_VALID", is2FAValidated);

        return Jwts.builder()
            .claims(claims)
            .subject(user.getEmail())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + expirationTime)) // expiry should be the half hour
            .signWith(getKey())
            .compact();
    }


    public SecretKey getKey() {
        // WHAT IS BASE64 ENCODING AND WHY IS IT IMPORTANT ?
        // When encrypting data (like passwords), the output is usually binary data (raw bytes).
        // Problem: Binary data is not human-readable and may contain special characters that can cause issues when storing or transmitting the data.
        //✅ Solution: Base64 encoding converts binary data into a text format that can be safely stored or transmitted.
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractClaims(token);
        return claimResolver.apply(claims);
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
            .verifyWith(getKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    // so now here the real question is like why am i taking the userDetails here
    public boolean validateToken(String token, UserDetails userDetails) {
        String userEmail = extractSubject(token);
        return userEmail.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiry(token).before(new Date());
    }

    public Date extractExpiry(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public ResponseEntity<String> extractToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            return ResponseEntity.badRequest().body("Invalid Authorization header");
        }

        return ResponseEntity.ok(authHeader.substring(7));
    }
}


// now it been giving me the error of the jwt token expired which is clearly not
