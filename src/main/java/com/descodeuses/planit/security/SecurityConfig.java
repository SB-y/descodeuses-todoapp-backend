
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration // Indique que cette classe contient des configurations Spring
@EnableWebSecurity // Active la sécurité Web avec Spring Security
public class SecurityConfig {

    // Injection du filtre JWT personnalisé
    @Autowired
    private JwtFilter jwtFilter;

    // Valeur à changer dans application.properties en fonction de l'environnement
    // (en local, en ligne)
    @Value("${allowCorsOrigin}")
    private String allowCorsOrigin;

    // Bean pour exposer un AuthenticationManager, utile pour l'authentification
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // Configuration de la chaîne de filtres de sécurité
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource)
            throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Désactive la protection CSRF (inutile avec JWT car stateless)
                .cors(cors -> cors.configurationSource(corsConfigurationSource)) // Active la configuration CORS
                .authorizeHttpRequests(auth -> auth
                        // Autoriser toutes les requêtes OPTIONS (préflight CORS)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/auth/**").permitAll() // Autorise les requêtes non authentifiées vers //
                                                                 // /auth/**
                        .requestMatchers("/api/**").hasAnyRole("USER", "ADMIN") // Nécessite un rôle USER ou ADMIN pour
                                                                                // /api/**
                        .anyRequest().authenticated() // Toutes les autres requêtes doivent être authentifiées
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // Déclare que l'application ne maintient pas de session (stateless)

        // Ajoute le filtre JWT avant le filtre d'authentification par défaut
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build(); // Construit la chaîne de filtres
    }

    // Bean pour encoder les mots de passe avec l'algorithme BCrypt (très sécurisé)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configuration du CORS pour autoriser les appels depuis le frontend Angular
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // split la chaîne en liste d'origines
        //List<String> allowedOrigins = List.of(allowCorsOrigin.split(","));
        config.setAllowedOrigins(List.of(allowCorsOrigin.split(",")));
       
        // Méthodes HTTP autorisées
        config.setAllowedMethods(List.of("*"));
        
        config.setAllowedHeaders(List.of("*")); // Autorise tous les headers
        config.setAllowCredentials(true); // Autorise les cookies et les headers d’authentification

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // Applique cette configuration à toutes les routes
        return source;
    }

}