package com.Ariel_Rom.Expense_Tracker_API.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service // Servicio para manejar creación y validación de tokens JWT
public class JWTService {

    // Clave secreta codificada en Base64URL para firmar los tokens
    private String SECRET_KEY = "6A5D4E3F2C1B0A9F8G7H6I5J4K3L2M1N0P9O8N7M6L5K4J3I2";

    // Genera un token JWT básico para un usuario (sin claims extra)
    public String getToken(UserDetails user){
        return getToken(new HashMap<>(), user);
    }

    // Genera el token JWT incluyendo claims extras (pueden ser custom)
    private String getToken(Map<String, Object> extraClaims, UserDetails user){
        return Jwts.builder()
                .setClaims(extraClaims)                     // Claims personalizados
                .setSubject(user.getUsername())              // Subject: username del usuario
                .setIssuedAt(new Date(System.currentTimeMillis()))  // Fecha de emisión
                // Fecha de expiración: ahora * 1000 * 60 * 60 * 24 (ojo, acá hay un bug, debería ser suma no multiplicación)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getKey(), SignatureAlgorithm.HS256) // Firma con clave secreta y algoritmo HS256
                .compact();                                  // Genera el token en string
    }

    // Obtiene la clave secreta decodificada para firmar/verificar el token
    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Extrae el username del token (del claim "subject")
    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    // Valida que el token sea válido: username coincida y que no haya expirado
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Verifica si el token ya expiró
    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }

    // Obtiene la fecha de expiración del token
    private Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    // Obtiene cualquier claim del token usando un Function para mapearlo
    public  <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extrae todos los claims del token, validando la firma con la clave secreta
    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

