// Gère les opérations métier sur les utilisateurs (CRUD, profil, conversion DTO...).
//Récupère un utilisateur par son ID ou username
//Met à jour un profil
//Supprime un utilisateur (et avant cela supprimer ses taches, ses contacts, ses projets, ses messages)
//Convertit entre entité et DTO
//Gère les rôles et infos personnelles (hors mot de passe généralement)
//Gère la création d’un nouvel utilisateur (inscription)

// Déclare que cette classe fait partie du package com.descodeuses.planit.service
package com.descodeuses.planit.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
// Indique que c’est un service Spring, donc une classe métier réutilisable
import org.springframework.stereotype.Service;

import com.descodeuses.planit.dto.RegisterRequest;
import com.descodeuses.planit.dto.UtilisateurDTO;
import com.descodeuses.planit.entity.ActionEntity;

// Import de l’entité Utilisateur (celle qui correspond à la table des utilisateurs)
import com.descodeuses.planit.entity.UtilisateurEntity;
import com.descodeuses.planit.repository.ActionRepository;
import com.descodeuses.planit.repository.ContactRepository;
import com.descodeuses.planit.repository.ProjetRepository;
// Import du repository (interface permettant d'accéder aux utilisateurs en base)
import com.descodeuses.planit.repository.UtilisateurRepository;

// Exception lancée si l’utilisateur n’est pas trouvé
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

// Indique à Spring que cette classe est un service (composant de la couche métier)
@Service
public class UserService {

    // Attribut privé pour accéder aux méthodes du repository
    private final UtilisateurRepository utilisateurRepository;

    private final PasswordEncoder passwordEncoder;

    private final ActionRepository actionRepository;
    private final ContactRepository contactRepository;
    private final ProjetRepository projetRepository;

    // Constructeur avec injection du repository (Spring s’en occupe)
    public UserService(UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder,
            ActionRepository actionRepository, ContactRepository contactRepository, ProjetRepository projetRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
        this.actionRepository = actionRepository;
        this.projetRepository = projetRepository;
        this.contactRepository = contactRepository;

    }

    // Crée un utilisateur
    public void register(RegisterRequest request) {

        if (utilisateurRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Utilisateur déjà existant");
        }

        UtilisateurEntity user = new UtilisateurEntity();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // OBLIGATOIRE
        user.setName(request.getName());
        user.setSurname(request.getSurname());
        user.setGenre(request.getGenre());
        user.setRole("ROLE_USER");

        utilisateurRepository.save(user);
    }




    // Méthode publique pour rechercher un utilisateur par son nom d'utilisateur
    // (username)
    public UtilisateurEntity findByUsername(String username) {
        // Recherche dans la base via le repository ; si rien trouvé, lève une exception
        // claire
        return utilisateurRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable avec le nom : " + username));
    }

    // Méthode publique pour avoir l'utilisateur actuellement connecté
    public UtilisateurEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return utilisateurRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));
    }

    // Récupère utilisateur par id
    public UtilisateurEntity getById(Long id) {
        return utilisateurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé avec id: " + id));
    }

    // Sécurise les pages profil (accessibles par l'utilisateur concerné et l'admin)
    public UtilisateurDTO getByIdSecure(Long id, Authentication auth) {

        UtilisateurEntity current = findByUsername(auth.getName());
        UtilisateurEntity target = getById(id);

        boolean isOwner = current.getId().equals(target.getId());
        boolean isAdmin = current.getRole().equals("ROLE_ADMIN");

        if (!isOwner && !isAdmin) {
            throw new SecurityException("Vous n'êtes pas autorisé à consulter ce profil.");
        }

        return convertToDTO(target);
    }

     // Méthode publique pour avoir tous les utilisateurs
     public List<UtilisateurEntity> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }




    // Met à jour le profil
    public UtilisateurDTO update(Long id, UtilisateurDTO dto) {
        UtilisateurEntity existingEntity = utilisateurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé avec id: " + id));

        existingEntity = converttoEntity(existingEntity, dto);

        UtilisateurEntity updatedEntity = utilisateurRepository.save(existingEntity);
        return convertToDTO(updatedEntity);

    }



    // Supprime le profil
    @Transactional
    public void delete(Long id) {
        UtilisateurEntity user = utilisateurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé avec id: " + id));

        // Retirer l'utilisateur des tâches où il est assigné
        List<ActionEntity> assignedTasks = actionRepository.findByUtilisateursAssignesContaining(user);
        for (ActionEntity task : assignedTasks) {
            task.getUtilisateursAssignes().remove(user);
            actionRepository.save(task);
        }

        // Supprimer toutes les tâches dont il est propriétaire
        List<ActionEntity> ownedTasks = actionRepository.findByUtilisateur(user);
        actionRepository.deleteAll(ownedTasks);

        // Supprimer tous ses contacts
        contactRepository.deleteAll(contactRepository.findByUtilisateur(user));

        // Supprimer tous ses projets
        projetRepository.deleteAll(projetRepository.findByUtilisateur(user));

        // Enfin supprimer l’utilisateur
        utilisateurRepository.delete(user);
    }

    // Méthodes dto-entity
    public UtilisateurDTO convertToDTO(UtilisateurEntity utilisateur) {
        // Crée un DTO à partir de l'entité de base
        UtilisateurDTO dto = new UtilisateurDTO(
                utilisateur.getId(),
                utilisateur.getUsername(),
                utilisateur.getRole(),
                utilisateur.getName(),
                utilisateur.getSurname(),
                utilisateur.getGenre());

        return dto;
    }

    public UtilisateurEntity converttoEntity(UtilisateurEntity entity, UtilisateurDTO dto) {
        entity.setUsername(dto.getUsername());

        // Ne pas écraser le rôle si non fourni dans le DTO
        if (dto.getRole() != null && !dto.getRole().isEmpty()) {
            entity.setRole(dto.getRole());
        }

        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setGenre(dto.getGenre());

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            // Ici, il faut hasher le mot de passe avant de le setter
            String hashedPassword = passwordEncoder.encode(dto.getPassword());
            entity.setPassword(hashedPassword);
        }

        return entity;
    }

    /*
     * // Supprime un utilisateur
     * public void delete(Long id) {
     * if (!repository.existsById(id)) {
     * throw new EntityNotFoundException("Projet non trouvé avec id: " + id);
     * }
     * repository.deleteById(id);
     * }
     */

}