package com.descodeuses.planit.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


import com.descodeuses.planit.dto.ProjetDTO;
import com.descodeuses.planit.entity.ProjetEntity;
import com.descodeuses.planit.entity.UtilisateurEntity;
import com.descodeuses.planit.repository.ProjetRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProjetService {

    private final ProjetRepository repository;
    private final UserService utilisateurService;

    public ProjetService(ProjetRepository repository, UserService utilisateurService) {
        this.repository = repository;
        this.utilisateurService = utilisateurService;
    }

    private ProjetDTO convertToDTO(ProjetEntity projet) {
        return new ProjetDTO(
            projet.getId(),
            projet.getTitle(),
            projet.getDescription()
        );
    }

    private ProjetEntity convertToEntity(ProjetDTO projetDTO) {
        ProjetEntity projet = new ProjetEntity();
        projet.setId(projetDTO.getId());
        projet.setTitle(projetDTO.getTitle());
        projet.setDescription(projetDTO.getDescription());
        return projet;
    }

    // Récupère tous les projets de l'utilisateur connecté
    public List<ProjetDTO> getAllByUser(Authentication authentication) {
        String username = authentication.getName();
        UtilisateurEntity user = utilisateurService.findByUsername(username);
        List<ProjetEntity> projets = repository.findByUtilisateur(user);
        return projets.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Récupère un projet par ID (vérifie que l'utilisateur est propriétaire)
    public ProjetDTO getById(Long id, Authentication authentication) {
        ProjetEntity projet = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Projet non trouvé avec id: " + id));

        String username = authentication.getName();
        UtilisateurEntity user = utilisateurService.findByUsername(username);

        if (!projet.getUtilisateur().getId().equals(user.getId())) {
            throw new SecurityException("Accès refusé");
        }

        return convertToDTO(projet);
    }

    // Crée un nouveau projet lié à l'utilisateur connecté
    public ProjetDTO create(ProjetDTO projetDTO, Authentication authentication) {
        ProjetEntity projet = convertToEntity(projetDTO);

        String username = authentication.getName();
        UtilisateurEntity user = utilisateurService.findByUsername(username);
        projet.setUtilisateur(user);

        ProjetEntity saved = repository.save(projet);
        return convertToDTO(saved);
    }

    // Met à jour un projet existant (vérifie propriété)
    public ProjetDTO update(Long id, ProjetDTO projetDTO, Authentication authentication) {
        ProjetEntity existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Projet non trouvé avec id: " + id));

        String username = authentication.getName();
        UtilisateurEntity user = utilisateurService.findByUsername(username);

        if (!existing.getUtilisateur().getId().equals(user.getId())) {
            throw new SecurityException("Accès refusé");
        }

        existing.setTitle(projetDTO.getTitle());
        existing.setDescription(projetDTO.getDescription());

        ProjetEntity updated = repository.save(existing);
        return convertToDTO(updated);
    }

    // Supprime un projet (vérifie propriété)
    public void delete(Long id, Authentication authentication) {
        ProjetEntity projet = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Projet non trouvé avec id: " + id));

        String username = authentication.getName();
        UtilisateurEntity user = utilisateurService.findByUsername(username);

        if (!projet.getUtilisateur().getId().equals(user.getId())) {
            throw new SecurityException("Accès refusé");
        }

        repository.deleteById(id);
    }
}
