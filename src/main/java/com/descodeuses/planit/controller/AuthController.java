// Contrôleur REST qui gère l’authentification et l’inscription des utilisateurs.
// Fonctionnalités :
// - Authentifier un utilisateur avec ses identifiants (POST /auth/login)
//   → Vérifie les identifiants via Spring Security
//   → Extrait le rôle de l’utilisateur
//   → Génère un token JWT et le retourne avec le rôle
// - Inscrire un nouvel utilisateur (POST /auth/register)
//   → Délègue la création de l’utilisateur à UserService
//   → Retourne un message de confirmation
// Utilise AuthService pour la logique JWT, UserService pour la gestion des utilisateurs,
// et AuthenticationManager de Spring Security pour valider les identifiants.



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

// Indique que cette classe est un contrôleur REST (retourne du JSON)
@RestController
// Toutes les routes de ce contrôleur commenceront par /auth
@RequestMapping("/auth")
public class AuthController {

    // Dépendances injectées via le constructeur
    private final AuthService authService;          // Service gérant la logique d’authentification (JWT)
    private final LogDocumentService logService;    // Service de logging (non utilisé directement ici)
    private final UserService userService;          // Service pour gérer les utilisateurs (inscription, etc.)
   
    // Constructeur : injection des dépendances principales
    public AuthController(AuthService authService, LogDocumentService logService, UserService userService) {
        this.authService = authService;
        this.logService = logService;
        this.userService = userService;
    }

    // Gestionnaire d’authentification de Spring Security (injecté automatiquement)
    @Autowired
    private AuthenticationManager authenticationManager;

    // Endpoint POST /auth/login → authentification de l’utilisateur
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request, HttpServletRequest httpRequest) {
        // Étape 1 : Authentifier l’utilisateur avec Spring Security
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        // Étape 2 : Extraire le rôle (ici, on prend le premier rôle trouvé)
        String role = auth.getAuthorities()                // retourne une Collection<GrantedAuthority>
                .stream()                                  // transforme en flux
                .map(GrantedAuthority::getAuthority)       // récupère la valeur du rôle
                .findFirst().orElse("");                   // si aucun rôle, retourne ""

        // Étape 3 : Générer le JWT via le service d’authentification
        String jwt = authService.login(request.getUsername(), request.getPassword());

        // Étape 4 : Retourner le token JWT et le rôle de l’utilisateur dans la réponse
        return ResponseEntity.ok(new AuthResponse(jwt, role));
    }

    // Endpoint POST /auth/register → inscription d’un nouvel utilisateur
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody UtilisateurEntity user) {
        userService.register(user); // délègue au service UserService la création du compte
        // Retourne un message de confirmation
        return ResponseEntity.ok(Map.of("message", "Inscription réussie !"));
    }
}
