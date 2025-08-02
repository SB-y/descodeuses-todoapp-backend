// JwtUtil sert à créer, lire et valider les tokens JWT côté backend pour sécuriser les échanges 
//entre le client et le serveur.

package com.descodeuses.planit.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component // Composant Spring, disponible pour l'injection
public class JwtUtil {

    // Clé secrète encodée en Base64 (minimum 256 bits pour HS256)
    private static final String SECRET_KEY = "YWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWE=";

    // Extrait le nom d'utilisateur (subject) depuis le token JWT
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extrait la date d'expiration du token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Méthode générique qui extrait n'importe quelle information depuis les claims
    // du token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extrait tous les claims (informations) contenus dans le token JWT
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // Utilise la clé secrète pour décoder
                .build()
                .parseClaimsJws(token) // Parse et valide la signature du token
                .getBody(); // Récupère le corps du token (les claims)
    }

    // Vérifie si le token est expiré en comparant la date d'expiration avec la date
    // actuelle
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Génère un token JWT à partir d'un UserDetails (utilisateur)
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        // Récupère la liste des rôles de l'utilisateur et les ajoute dans les claims du
        // token
        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(auth -> auth.getAuthority())
                .collect(Collectors.toList());

        claims.put("roles", roles);

        // Crée le token en utilisant le sujet (username) et les claims
        return createToken(claims, userDetails.getUsername());
    }

    // Crée effectivement le token JWT avec les claims, le sujet, la date d'émission
    // et d'expiration
    private String createToken(Map<String, Object> claims, String subject) {
        long validityInMs = 1000 * 60 * 60 * 10; // Durée de validité : 10 heures

        return Jwts.builder()
                .setClaims(claims) // Claims ajoutés (ex : rôles)
                .setSubject(subject) // Sujet du token (nom d'utilisateur)
                .setIssuedAt(new Date(System.currentTimeMillis())) // Date d'émission
                .setExpiration(new Date(System.currentTimeMillis() + validityInMs)) // Date d'expiration
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Signature avec clé et algorithme
                .compact(); // Génère le token sous forme de chaîne compacte
    }

    // Valide le token en vérifiant que le username correspond et que le token n'est
    // pas expiré
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Retourne la clé secrète utilisée pour signer et vérifier le token,
    // en décodant la clé Base64 et en la convertissant en Key compatible avec HS256
    public Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}