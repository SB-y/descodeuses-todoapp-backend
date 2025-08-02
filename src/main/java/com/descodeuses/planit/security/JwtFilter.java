// JwtFilter vérifie chaque requête entrante pour voir si un token JWT est présent 
//et valide, puis authentifie l'utilisateur si c’est le cas.


package com.descodeuses.planit.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

@Component // Indique que ce filtre est un composant Spring à gérer par le conteneur
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil; // Utilitaire pour manipuler les tokens JWT

    @Autowired
    private UserDetailsService userDetailsService; // Pour charger les informations d'un utilisateur

    // Méthode appelée à chaque requête HTTP (filtre exécuté une seule fois par requête)
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // Ignore les requêtes envoyées aux endpoints d'authentification (ex:/auth/login)
        String path = request.getServletPath();
        if (path.startsWith("/auth/")) {
            filterChain.doFilter(request, response); // Poursuit sans vérification JWT
            return;
        }

        // Récupère le header Authorization de la requête HTTP
        final String authHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        // Vérifie que le header commence bien par "Bearer " et extrait le token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7); // Supprime "Bearer " pour ne garder que le token
            username = jwtUtil.extractUsername(jwt); // Extrait le nom d'utilisateur du token
        }

        // Si un username est trouvé et qu'aucune auth n'est déjà présente dans le contexte
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username); // Charge l'utilisateur depuis la
                                                                                       // DB ou mémoire

            // Vérifie la validité du token (signature, expiration, correspondance user)
            if (jwtUtil.validateToken(jwt, userDetails)) {

                // Récupère les "claims" (informations) stockées dans le token
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(jwtUtil.getSigningKey()) // Clé utilisée pour signer/valider
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody();

                // Récupère la liste des rôles depuis les claims JWT
                List<String> roles = claims.get("roles", List.class);

                // Transforme chaque rôle en autorité Spring Security
                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                // Crée un objet d'authentification avec les infos utilisateur et ses rôles
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // Pas besoin de mot de passe ici
                        authorities);

                // Ajoute des infos supplémentaires sur la requête (comme l'adresse IP)
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Enregistre l'objet d'authentification dans le contexte de sécurité
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Poursuit la chaîne des filtres
        filterChain.doFilter(request, response);
    }
}