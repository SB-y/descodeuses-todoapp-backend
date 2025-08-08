// Déclare le package dans lequel se trouve ce contrôleur
package com.descodeuses.planit.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.descodeuses.planit.dto.AuthRequest;
import com.descodeuses.planit.dto.AuthResponse;
import com.descodeuses.planit.entity.UtilisateurEntity;
import com.descodeuses.planit.service.AuthService;
import com.descodeuses.planit.service.LogDocumentService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final LogDocumentService logService;

    public AuthController(AuthService authService, LogDocumentService logService) {
        this.authService = authService;
        this.logService = logService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request, HttpServletRequest httpRequest) {
        String jwt = authService.login(request.getUsername(), request.getPassword());
        // journalisation etc.
        return ResponseEntity.ok(new AuthResponse(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody UtilisateurEntity user) {
        authService.register(user);
        return ResponseEntity.ok(Map.of("message", "Inscription réussie !"));
    }
}