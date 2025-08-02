// Déclare le package dans lequel se trouve ce contrôleur
package com.descodeuses.planit.controller;

// Importe la classe Map pour retourner des réponses sous forme de clé/valeur
import java.util.Map;

// Importe le service de journalisation personnalisé
import com.descodeuses.planit.service.LogDocumentService;

// Importe les classes nécessaires pour récupérer des infos sur la requête HTTP
import jakarta.servlet.http.HttpServletRequest;

// Importe les annotations Spring pour l'injection de dépendances et la gestion des requêtes
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Importe les DTO pour la gestion des requêtes/réponses d'authentification
import com.descodeuses.planit.dto.AuthRequest;
import com.descodeuses.planit.dto.AuthResponse;

// Importe l'entité utilisateur et le repository associé
import com.descodeuses.planit.entity.UtilisateurEntity;
import com.descodeuses.planit.repository.UtilisateurRepository;

// Importe les classes liées à la sécurité et au JWT
import com.descodeuses.planit.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

// Indique que cette classe est un contrôleur REST (qui répond aux requêtes HTTP avec des objets JSON)
@RestController
// Préfixe toutes les routes de ce contrôleur avec "/auth"
@RequestMapping("/auth")
public class AuthController {

    // Injecte le gestionnaire d’authentification Spring Security
    @Autowired
    private AuthenticationManager authenticationManager;

    // Injecte l'utilitaire maison pour générer des tokens JWT
    @Autowired
    private JwtUtil jwtUtil;

    // Injecte le service de gestion des utilisateurs (pour charger les détails d'un utilisateur)
    @Autowired
    private UserDetailsService userDetailsService;

    // Injecte le service de log (MongoDB) pour journaliser les actions
    @Autowired
    private LogDocumentService logService;

    // Point de terminaison POST pour la connexion (/auth/login)
    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authRequest, HttpServletRequest request) {
        // Authentifie l'utilisateur avec son username et mot de passe
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

        // Récupère les détails de l'utilisateur
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        
        // Génère un token JWT à partir des détails utilisateur
        final String jwt = jwtUtil.generateToken(userDetails);

        // Récupère l'entité utilisateur depuis la base de données
        UtilisateurEntity user = utilisateurRepository.findByUsername(authRequest.getUsername()).orElse(null);

        // Enregistre un log de la tentative de connexion dans MongoDB
        logService.addLog("Login called", request, authRequest, user);

        // Retourne le token JWT dans la réponse
        return ResponseEntity.ok(new AuthResponse(jwt));
    }

    // Injecte le repository des utilisateurs pour accéder à la base de données
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    // Injecte l'encodeur de mots de passe (BCrypt ou autre)
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Point de terminaison POST pour l'inscription (/auth/register)
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UtilisateurEntity user) {

        // Vérifie si le nom d'utilisateur est déjà utilisé dans la base
        if (utilisateurRepository.findByUsername(user.getUsername()).isPresent()) {
            // Retourne une erreur si l'utilisateur existe déjà
            return ResponseEntity.badRequest().body("Cet email est déjà utilisé.");
        }

        // Encode le mot de passe avant de le sauvegarder
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Définit un rôle par défaut pour le nouvel utilisateur
        user.setRole("ROLE_USER");

        // Sauvegarde l'utilisateur dans la base de données
        utilisateurRepository.save(user);

        // Retourne une réponse de succès
        return ResponseEntity.ok(Map.of("message", "Inscription réussie !"));
    }

}
