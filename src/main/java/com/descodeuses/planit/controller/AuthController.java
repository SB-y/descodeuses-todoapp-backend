// Déclare le package dans lequel se trouve ce contrôleur
package com.descodeuses.planit.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.descodeuses.planit.dto.AuthRequest;
import com.descodeuses.planit.dto.AuthResponse;
import com.descodeuses.planit.entity.UtilisateurEntity;
import com.descodeuses.planit.service.AuthService;
import com.descodeuses.planit.service.LogDocumentService;
import com.descodeuses.planit.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final LogDocumentService logService;
    private final UserService userService;
   

    public AuthController(AuthService authService, LogDocumentService logService, UserService userService) {
        this.authService = authService;
        this.logService = logService;
        this.userService = userService;
    }

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request, HttpServletRequest httpRequest) {
        // Étape 1 : Authentifier l’utilisateur avec Spring Security
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        // Étape 2 : Extraire le rôle (on prend le premier rôle trouvé)
        String role = auth.getAuthorities() // retourne une Collection<GrantedAuthority>
                .stream() // transforme en Stream<GrantedAuthority>
                .map(GrantedAuthority::getAuthority) // applique getAuthority() sur chaque élément
                .findFirst().orElse(""); // Prend le premier rôle trouvé (ou une chaîne vide si aucun)

        // Étape 3 : Générer le JWT avec le service d’authentification
        String jwt = authService.login(request.getUsername(), request.getPassword());

        // Étape 4 : Retourner le token et le rôle dans la réponse
        return ResponseEntity.ok(new AuthResponse(jwt, role));
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody UtilisateurEntity user) {
        userService.register(user);
        return ResponseEntity.ok(Map.of("message", "Inscription réussie !"));
    }
}