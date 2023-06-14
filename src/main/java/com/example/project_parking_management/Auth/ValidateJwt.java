package com.example.project_parking_management.Auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class ValidateJwt {
    private static final String SECRET_KEY = "yourSecretKey";

    public static String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
