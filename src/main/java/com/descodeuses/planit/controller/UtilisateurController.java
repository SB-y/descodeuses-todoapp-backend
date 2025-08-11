package com.descodeuses.planit.controller;

import com.descodeuses.planit.dto.UtilisateurDTO;
import com.descodeuses.planit.entity.UtilisateurEntity;
import com.descodeuses.planit.service.UserService;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
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

    @GetMapping
    public ResponseEntity<List<UtilisateurDTO>> getAllUtilisateurs() {
        List<UtilisateurEntity> utilisateurs = userService.getAllUtilisateurs();

        List<UtilisateurDTO> dtos = utilisateurs.stream()
                .map(userService::convertToDTO) // Appel méthode de conversion dans le service
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/monprofil")
    public ResponseEntity<UtilisateurDTO> getCurrentUser() {
        UtilisateurEntity currentUser = userService.getCurrentUser();
        UtilisateurDTO dto = userService.convertToDTO(currentUser);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UtilisateurDTO> getUtilisateurById(@PathVariable Long id) {
        try {
            UtilisateurEntity utilisateur = userService.getById(id);
            UtilisateurDTO dto = userService.convertToDTO(utilisateur);
            return ResponseEntity.ok(dto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/monprofil")
    public ResponseEntity<UtilisateurDTO> update(@RequestBody UtilisateurDTO dto) {
        UtilisateurEntity currentUser = userService.getCurrentUser();
        Long id = currentUser.getId();

        UtilisateurDTO updated = userService.update(id, dto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UtilisateurDTO> updateById(@PathVariable Long id, @RequestBody UtilisateurDTO dto) {
        UtilisateurDTO updated = userService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUtilisateur(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

}