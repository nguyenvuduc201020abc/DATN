package com.example.project_parking_management.Auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.JwtBuilder;
import java.util.Date;

public class JwtUtil {
    private static final String SECRET_KEY = "Nguyenvuduc201020@";
    private static final long EXPIRATION_TIME = 86400000; // 24 giờ

    public static String generateToken(String username, String password, String role) {
        JwtBuilder builder = Jwts.builder()
                .claim("username", username)
                .claim("password", password)
                .claim("role", role)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY);

        return builder.compact();
    }

    public static Claims decodeToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}

