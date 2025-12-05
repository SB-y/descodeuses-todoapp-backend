package com.descodeuses.planit.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Bon choix pour le hachage des mots de passe
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter; // Import clé pour une gestion CORS robuste

@Configuration // Indique à Spring que cette classe contient des définitions de beans
@EnableWebSecurity // Active l'intégration de Spring Security
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter; // Filtre personnalisé qui va valider le JWT dans les requêtes

    @Value("${allowCorsOrigin}")
    private String allowCorsOrigin; // Configuration des origines autorisées depuis application.properties. Bon pour
                                    // la production.

    // ------------- AUTH MANAGER ------------
    /**
     * Expose l'AuthenticationManager comme un Bean.
     * Il est nécessaire pour l'authentification (e.g., dans le service
     * d'authentification)
     * pour vérifier les identifiants utilisateur (username/password).
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // ------------- CORS GLOBAL ET ROBUSTE ------------
    /**
     * Définit un filtre CORS global. C'est la méthode recommandée pour les API
     * REST.
     * Le CorsFilter est exécuté très tôt dans la chaîne, ce qui assure que les
     * requêtes
     * preflight (OPTIONS) sont gérées correctement avant toute vérification de
     * sécurité.
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true); // Autorise l'envoi de cookies/headers d'authentification
        config.setAllowedOrigins(List.of(allowCorsOrigin.split(","))); // Liste des origines autorisées (votre frontend
                                                                       // Angular)
        config.addAllowedHeader("*"); // Autorise tous les headers
        config.addAllowedMethod("*"); // Autorise toutes les méthodes HTTP (GET, POST, PUT, DELETE, OPTIONS, etc.)

        source.registerCorsConfiguration("/**", config); // Applique cette configuration à toutes les routes
        return new CorsFilter(source);
    }

    // ------------- SECURITY ------------
    /**
     * Définit la chaîne de filtres de sécurité. C'est le cœur de la configuration.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // 1. Désactive CSRF : Très bonne pratique pour les API sans état (stateless)
                // utilisant JWT, car le token n'est pas stocké en session (évitant la faille
                // CSRF classique).
                .csrf(csrf -> csrf.disable())

                // 2. Désactive la gestion CORS interne de Spring Security :
                // C'est logique car on gère le CORS via le CorsFilter global défini plus haut.
                .cors(cors -> cors.disable())

                // 3. Configuration de l'autorisation des requêtes.
                .authorizeHttpRequests(auth -> auth
                        // Autorise explicitement toutes les requêtes preflight OPTIONS sans
                        // authentification.
                        // C'est vital pour le bon fonctionnement de CORS.
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Autorise l'accès aux routes d'authentification (login, inscription) sans JWT.
                        .requestMatchers("/auth/**").permitAll()

                        // Exige le rôle USER ou ADMIN pour accéder à toutes les routes de l'API.
                        .requestMatchers("/api/**").hasAnyRole("USER", "ADMIN")

                        // Toutes les autres requêtes doivent être authentifiées par un JWT valide.
                        .anyRequest().authenticated())
                // 4. Gestion de Session : Définit la politique comme sans état.
                // Essentiel pour JWT car le token porte toutes les infos d'authentification.
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 5. Ajout du Filtre JWT : S'assure que notre filtre de vérification du JWT
        // est exécuté avant le filtre standard d'authentification par nom
        // d'utilisateur/mot de passe.
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build(); // Construction de la chaîne de filtres
    }

    /**
     * Définit l'encodeur de mot de passe à utiliser.
     * BCrypt est le standard de l'industrie pour le hachage sécurisé.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}