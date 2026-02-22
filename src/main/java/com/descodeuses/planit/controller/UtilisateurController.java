// Contrôleur REST qui gère les opérations liées aux utilisateurs.
// Fonctionnalités :
// - Récupérer tous les utilisateurs (GET /api/utilisateur)
// - Récupérer le profil de l’utilisateur connecté (GET /api/utilisateur/monprofil)
// - Récupérer un utilisateur par son ID (GET /api/utilisateur/{id})
// - Mettre à jour le profil de l’utilisateur connecté (PUT /api/utilisateur/monprofil)
// - Mettre à jour un utilisateur par son ID (PUT /api/utilisateur/{id})
// - Supprimer un utilisateur par son ID (DELETE /api/utilisateur/{id})
// Utilise UserService pour la logique métier (conversion entité/DTO, accès BDD, etc.).

// Déclare le package dans lequel se trouve ce contrôleur
package com.descodeuses.planit.controller;


import com.descodeuses.planit.dto.UtilisateurDTO;
import com.descodeuses.planit.entity.UtilisateurEntity;
import com.descodeuses.planit.service.UserService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Indique que cette classe est un contrôleur REST (retourne du JSON)
@RestController
// Toutes les routes de ce contrôleur commenceront par /api/utilisateur
@RequestMapping("/api/utilisateur")
public class UtilisateurController {

    // Service qui gère la logique métier liée aux utilisateurs
    private final UserService userService;

    // Constructeur avec injection du service UserService
    public UtilisateurController(UserService userService) {
        this.userService = userService;
    }

    // Endpoint GET /api/utilisateur
    // → Récupère la liste de tous les utilisateurs et la convertit en DTO
    @GetMapping
    public ResponseEntity<List<UtilisateurDTO>> getAllUtilisateurs() {
        List<UtilisateurEntity> utilisateurs = userService.getAllUtilisateurs();

        // Conversion des entités en DTO pour exposer uniquement les infos nécessaires
        List<UtilisateurDTO> dtos = utilisateurs.stream()
                .map(userService::convertToDTO)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    // Endpoint GET /api/utilisateur/monprofil
    // → Récupère l’utilisateur actuellement connecté (via Spring Security)
    @GetMapping("/monprofil")
    public ResponseEntity<UtilisateurDTO> getCurrentUser() {
        UtilisateurEntity currentUser = userService.getCurrentUser();
        UtilisateurDTO dto = userService.convertToDTO(currentUser);
        return ResponseEntity.ok(dto);
    }

    // Endpoint GET /api/utilisateur/{id}
    // → Récupère un utilisateur par son ID
    @GetMapping("/{id}")
    public ResponseEntity<UtilisateurDTO> getUtilisateurById(
            @PathVariable Long id, // Récupère l'ID dans l'URL
            Authentication auth) { // Injecte l'utilisateur actuellement authentifié

        // Appelle la version sécurisée du service :
        // - vérifie que le user existe
        // - vérifie que l'utilisateur connecté a le droit d'accéder à ce profil
        // (même utilisateur OU admin)
        UtilisateurDTO dto = userService.getByIdSecure(id, auth);

        return ResponseEntity.ok(dto);
    }

    // Endpoint PUT /api/utilisateur/monprofil
    // → Met à jour le profil de l’utilisateur connecté
    @PutMapping("/monprofil")
    public ResponseEntity<UtilisateurDTO> update(@Valid @RequestBody UtilisateurDTO dto) {
        UtilisateurEntity currentUser = userService.getCurrentUser();
        Long id = currentUser.getId();

        UtilisateurDTO updated = userService.update(id, dto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    // Endpoint PUT /api/utilisateur/{id}
    // → Met à jour un utilisateur identifié par son ID
    @PutMapping("/{id}")
    public ResponseEntity<UtilisateurDTO> updateById(@PathVariable Long id, @Valid @RequestBody UtilisateurDTO dto) {
        UtilisateurDTO updated = userService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    // Endpoint DELETE /api/utilisateur/{id}
    // → Supprime un utilisateur par son ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUtilisateur(@PathVariable Long id) {
        userService.delete(id);
        // Retourne 204 No Content si la suppression a réussi
        return ResponseEntity.noContent().build();
    }

}
