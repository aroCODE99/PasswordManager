package com.aro.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtService {

    // Replace this with a secure key in a real application, ideally fetched from environment variables
    // I am leaving this for the future aro plz solve this by finding out how to get the secret key and how to store one
    public static final String secretKey = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";


    public String generateTheToken(String userEmail) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
            .claims()
            .add(claims)
            .subject(userEmail)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
            .and()
            .signWith(getKey())
            .compact();
    }

    public SecretKey getKey() {
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

    private Claims extractClaims(String token) {
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
}