package org.appointment.backend.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {


    // Rastgele güçlü bir anahtar kullanın
    private final String secretKey = Base64.getEncoder().encodeToString("BuÇokGüçlüBirGizliAnahtarOlsun12345!".getBytes());

    private final long expirationTime = 86400000; // 1 gün (milisaniye cinsinden)

    // Token oluşturma
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // 1 gün
                .signWith(SignatureAlgorithm.HS256, secretKey) // HMAC-SHA256 algoritması
                .compact();
    }

    // Token doğrulama
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Kullanıcı adını token'dan çıkartma
    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
