// Service qui gère la logique d’authentification
// - utilise UserDetailsServiceImpl pour charger les utilisateurs
// - valide les identifiants via AuthenticationManager
// - gère la génération du JWT

package com.descodeuses.planit.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.descodeuses.planit.entity.UtilisateurEntity;
import com.descodeuses.planit.repository.UtilisateurRepository;
import com.descodeuses.planit.security.JwtUtil;

// Indique que cette classe est un service Spring (logique métier)
@Service
public class AuthService {

    // Dépendances injectées
    private final UtilisateurRepository utilisateurRepository; // Accès aux utilisateurs (BDD)
    private final PasswordEncoder passwordEncoder;             // Encode / vérifie les mots de passe
    private final AuthenticationManager authenticationManager; // Valide l’authentification
    private final JwtUtil jwtUtil;                             // Génère et valide les JWT
    private final UserDetailsService userDetailsService;       // Charge les infos utilisateur (implémentation Spring Security)

    // Constructeur avec injection des dépendances
    public AuthService(UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, JwtUtil jwtUtil,
                       UserDetailsService userDetailsService) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    // Méthode de connexion
    // 1. Vérifie les identifiants via AuthenticationManager
    // 2. Charge l’utilisateur via UserDetailsService
    // 3. Génère un JWT à partir des infos de l’utilisateur
    public String login(String username, String password) {
        // Étape 1 : authentifie l’utilisateur (exception si identifiants invalides)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        // Étape 2 : charge les détails de l’utilisateur
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Étape 3 : génère et retourne le token JWT
        return jwtUtil.generateToken(userDetails);
    }

}
