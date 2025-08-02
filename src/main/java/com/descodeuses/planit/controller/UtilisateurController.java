package com.descodeuses.planit.controller;

import com.descodeuses.planit.dto.UtilisateurDTO;
import com.descodeuses.planit.entity.UtilisateurEntity;
import com.descodeuses.planit.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/utilisateur")
public class UtilisateurController {

    private final UserService userService;

    public UtilisateurController(UserService userService) {
        this.userService = userService;
    }

//Indique que cette méthode doit répondre à une requête HTTP GET vers /api/utilisateur (ou autre, selon ton @RequestMapping de classe)
@GetMapping
// Cette méthode retourne une réponse HTTP contenant une liste de DTOs de type UtilisateurDTO
public ResponseEntity<List<UtilisateurDTO>> getAllUtilisateurs() {

    // Appelle le service pour récupérer la liste des entités Utilisateur depuis la base de données
    List<UtilisateurEntity> utilisateurs = userService.getAllUtilisateurs();

    // Transforme chaque UtilisateurEntity en UtilisateurDTO en utilisant Java Streams
    // Cela permet de ne pas exposer toute l'entité (relations JPA, mots de passe, etc.)
    List<UtilisateurDTO> dtos = utilisateurs.stream()
        // Pour chaque entité, on crée un nouvel objet UtilisateurDTO avec uniquement les infos nécessaires
        .map(u -> new UtilisateurDTO(u.getId(), u.getUsername(), u.getPassword(), u.getRole(), u.getName(), u.getSurname(), u.getGenre()))
        // Collecte tous les DTOs dans une liste
        .toList();

    // Renvoie la liste des DTOs avec un statut HTTP 200 OK dans la réponse
    return ResponseEntity.ok(dtos);
}
}