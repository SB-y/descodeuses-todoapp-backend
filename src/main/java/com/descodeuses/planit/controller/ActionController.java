// Contrôleur REST qui gère les opérations sur les actions (tâches).
// Fonctionnalités :
// - Récupérer une action par son ID (GET /api/action/{id})
// - Récupérer toutes les actions de l’utilisateur connecté (GET /api/action)
// - Créer une nouvelle action (POST /api/action) avec enregistrement d’un log dans MongoDB
// - Mettre à jour une action existante (PUT /api/action/{id}) avec enregistrement d’un log dans MongoDB
// - Supprimer une action (DELETE /api/action/{id})
// Utilise ActionService pour la logique métier et LogDocumentService pour le suivi des actions (logs).

// Déclaration du package dans lequel se trouve cette classe
package com.descodeuses.planit.controller;

// Import des classes nécessaires pour gérer les collections de données
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
// Import des classes Spring pour gérer les réponses HTTP et les statuts
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// Import des annotations Spring pour gérer les requêtes HTTP spécifiques
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Import des classes spécifiques au projet (DTO et service)
import com.descodeuses.planit.dto.ActionDTO;
import com.descodeuses.planit.service.ActionService;
import com.descodeuses.planit.service.LogDocumentService;
import com.descodeuses.planit.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

// Import d’autres annotations pour gérer les requêtes POST, PUT, path variables et corps de requête
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

// Indique que cette classe est un contrôleur REST capable de gérer des requêtes HTTP et retourner des réponses JSON
@RestController
// Définit le chemin de base pour toutes les requêtes gérées par ce contrôleur
@RequestMapping("/api/action")
public class ActionController {

    // Déclaration d’un service (couche métier) utilisé par ce contrôleur, en
    // injection via constructeur
    private final ActionService service;
    private final UserService userService;

    // Constructeur avec injection du service (Spring injecte automatiquement la
    // dépendance)
    public ActionController(ActionService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    // Méthode pour gérer les requêtes GET vers /api/action/{id} : récupération
    // d’une action par son id
    @GetMapping("/{id}")
    public ResponseEntity<ActionDTO> getActionById(@PathVariable Long id) {
        // Appel du service pour récupérer l’action correspondant à l’id
        ActionDTO actionDTO = service.getActionById(id);
        // Si l’action n’existe pas, on renvoie un statut HTTP 404 Not Found
        if (actionDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // Sinon on renvoie l’action avec un statut HTTP 200 OK
        return new ResponseEntity<>(actionDTO, HttpStatus.OK);
    }

    /*
     * // Méthode pour gérer les requêtes GET vers /api/action : récupération de
     * toutes les actions
     * 
     * @GetMapping
     * public ResponseEntity<List<ActionDTO>> getAll() {
     * // Appel du service pour récupérer la liste des actions
     * List<ActionDTO> actionsDTO = service.getAll(); // List<ActionDTO> = liste
     * d’objets ActionDTO.
     * // Renvoi de la liste avec un statut HTTP 200 OK
     * return new ResponseEntity<>(actionsDTO, HttpStatus.OK);
     * }
     */

    // Récupère toutes les actions de l'utilisateur connecté
    @GetMapping
    public ResponseEntity<List<ActionDTO>> getAllForUser(Authentication authentication) {
        List<ActionDTO> actionsDTO = service.getAllByUser(authentication);
        return new ResponseEntity<>(actionsDTO, HttpStatus.OK);
    }

    // Méthode pour gérer les requêtes POST vers /api/action : création d’une
    // nouvelle action
    @Autowired
    private LogDocumentService logDocumentService;

    @PostMapping
    public ResponseEntity<ActionDTO> create(@RequestBody ActionDTO newDTO, Authentication authentication,
            HttpServletRequest request) {

        // Création de l'action
        ActionDTO createdDTO = service.create(newDTO, authentication);

        // Logging de l'action créée dans MangoDB
        try {
            logDocumentService.addLog("Action created", request, createdDTO);
        } catch (Exception e) {
            System.err.println("⚠️ Impossible d'enregistrer le log MongoDB : " + e.getMessage());
        }
        

        // Retour de l'action avec statut 201
        return new ResponseEntity<>(createdDTO, HttpStatus.CREATED);
    }

    // Méthode pour gérer les requêtes PUT vers /api/action/{id} : mise à jour d’une
    // action existante
    @PutMapping("/{id}")
    public ResponseEntity<ActionDTO> update(@PathVariable Long id, @RequestBody ActionDTO dto,
            Authentication authentication, HttpServletRequest request) {

        // Appel du service pour mettre à jour l’action identifiée par l’id avec les
        // nouvelles données
        ActionDTO updated = service.update(id, dto, authentication);

        // Logging de l'action créée dans MangoDB
        try {
            logDocumentService.addLog("Action updated", request, updated);
        } catch (Exception e) {
            System.err.println("⚠️ Impossible d'enregistrer le log MongoDB : " + e.getMessage());
        }

        // Renvoi de l’action mise à jour avec un statut HTTP 200 OK
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    // Méthode pour gérer les requêtes DELETE vers /api/action/{id} : suppression
    // d’une action
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        // Appel du service pour supprimer l’action identifiée par l’id
        service.delete(id);

        // Renvoi d’une réponse sans contenu avec un statut HTTP 204 No Content au
        // client
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Récupère toutes les tâches où l’utilisateur connecté a été assigné
    @GetMapping("/assigned-to-me")
    public ResponseEntity<List<ActionDTO>> getAssignedToMe(Authentication authentication) {
        List<ActionDTO> tasks = service.getAssignedToMe(authentication);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

}