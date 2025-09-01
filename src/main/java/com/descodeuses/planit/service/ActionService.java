// Service qui gère la logique métier pour les actions (tâches)
// - CRUD complet (Create, Read, Update, Delete)
// - Conversion entre entités et DTO
// - Vérifie et rattache projets, contacts et utilisateur connecté



// Déclare que cette classe fait partie du package "service"
package com.descodeuses.planit.service;

// Imports nécessaires aux collections et types optionnels
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// Import pour gérer l'authentification Spring Security
import org.springframework.security.core.Authentication;

// Indique à Spring que c’est un service (injectable)
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

// Imports de DTOs
import com.descodeuses.planit.dto.ActionDTO;
import com.descodeuses.planit.dto.ContactDTO;
import com.descodeuses.planit.dto.ProjetDTO;

// Imports des entités JPA
import com.descodeuses.planit.entity.ActionEntity;
import com.descodeuses.planit.entity.ContactEntity;
import com.descodeuses.planit.entity.ProjetEntity;
import com.descodeuses.planit.entity.UtilisateurEntity;

// Imports des repositories (accès DB)
import com.descodeuses.planit.repository.ActionRepository;
import com.descodeuses.planit.repository.ContactRepository;
import com.descodeuses.planit.repository.ProjetRepository;
import com.descodeuses.planit.repository.UtilisateurRepository;

// Import pour lever une exception si une entité n'est pas trouvée
import jakarta.persistence.EntityNotFoundException;

// Déclare cette classe comme un composant Spring de type Service
@Service
public class ActionService {

    // Dépendances injectées (repositories et services)
    private final ActionRepository repository;
    private final ContactRepository contactRepository;
    private final ProjetRepository projetRepository;
    private final UserService userService;

    @Autowired
    private final UtilisateurRepository utilisateurRepository;

    // Constructeur avec injection des dépendances
    public ActionService(ActionRepository repository, ContactRepository contactRepository,
            ProjetRepository projetRepository, UtilisateurRepository utilisateurRepository, UserService userService) {
        this.repository = repository;
        this.contactRepository = contactRepository;
        this.projetRepository = projetRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.userService = userService;
    }

    // Méthode privée pour convertir une ActionEntity en ActionDTO
    private ActionDTO convertToDTO(ActionEntity action) {
        // Crée un DTO à partir de l'entité de base
        ActionDTO dto = new ActionDTO(
                action.getId(),
                action.getTitle(),
                action.getCompleted(),
                action.getDueDate(),
                action.getTextarea(),
                action.getPriorite());

        // Récupère uniquement les IDs des membres (pour formulaire)
        Set<Long> memberIds = action.getMembers().stream()
                .map(ContactEntity::getId)
                .collect(Collectors.toSet());
        dto.setMemberIds(memberIds);

        // Récupère les infos complètes des membres (pour affichage)
        Set<ContactDTO> membresDto = action.getMembers().stream()
                .map(c -> new ContactDTO(
                        c.getId(), c.getPrenom(), c.getNom(), c.getEmail(), c.getTel()))
                .collect(Collectors.toSet());
        dto.setMembres(membresDto);

        // Si un projet est associé, on remplit le DTO projet
        if (action.getProjet() != null) {
            ProjetEntity projet = action.getProjet();
            dto.setProjetId(projet.getId());
            dto.setProjet(new ProjetDTO(projet.getId(), projet.getTitle(), projet.getDescription()));
        }

        if (action.getUtilisateur() != null) {
            UtilisateurEntity user = action.getUtilisateur();
            dto.setUtilisateurId(user.getId());
            dto.setSurname(user.getSurname());
            dto.setName(user.getName());
            dto.setGenre(user.getGenre());
            dto.setUsername(user.getUsername()); // <-- OK à ajouter si besoin d’affichage
        }

        return dto;
    }

    // Convertit un DTO en entité (pour insertion ou update)
    private ActionEntity convertToEntity(ActionDTO actionDTO, Set<ContactEntity> members, ProjetEntity projet,
            UtilisateurEntity utilisateur) {
        ActionEntity action = new ActionEntity();
        action.setId(actionDTO.getId());
        action.setTitle(actionDTO.getTitle());
        action.setCompleted(actionDTO.getCompleted());
        action.setDueDate(actionDTO.getDueDate());
        action.setTextarea(actionDTO.getTextarea());
        action.setPriorite(actionDTO.getPriorite());
        action.setMembers(members);
        action.setProjet(projet);
        action.setUtilisateur(utilisateur);

        return action;
    }

    /*
     * // Récupère toutes les actions depuis la base de données
     * public List<ActionDTO> getAll() {
     * List<ActionEntity> entities = repository.findAll(); // toutes les entités
     * List<ActionDTO> dtos = new ArrayList<>(); // liste vide pour le résultat
     * 
     * for (ActionEntity item : entities) {
     * dtos.add(convertToDTO(item)); // conversion et ajout à la liste
     * }
     * return dtos;
     * }
     * 
     */

    // Récupère toutes les actions de l'utilisateur depuis la base de données
    public List<ActionDTO> getAllByUser(Authentication authentication) {
        // Récupère l'utilisateur connecté à partir de son nom d'utilisateur
        String username = authentication.getName();
        UtilisateurEntity utilisateur = userService.findByUsername(username);

        // Récupère uniquement les actions qui lui appartiennent
        List<ActionEntity> actions = repository.findByUtilisateur(utilisateur);

        // Convertit chaque ActionEntity en ActionDTO
        return actions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Récupère une seule action par son ID
    public ActionDTO getActionById(Long id) {
        Optional<ActionEntity> action = repository.findById(id);

        if (action.isEmpty()) {
            throw new EntityNotFoundException("Action not found with id: " + id);
        }

        ActionEntity entity = action.get();
        return convertToDTO(entity);
    }

    // Crée une nouvelle action à partir d’un DTO
    public ActionDTO create(ActionDTO dto, Authentication authentication) {
        ProjetEntity projet = null;

        // Si un ID de projet est fourni, on récupère le projet associé
        if (dto.getProjetId() != null) {
            projet = projetRepository.findById(dto.getProjetId())
                    .orElseThrow(
                            () -> new EntityNotFoundException("Projet introuvable avec l'id : " + dto.getProjetId()));
        }

        // Récupère tous les contacts dont les IDs sont dans le DTO
        Set<ContactEntity> contacts = new HashSet<>();
        if (dto.getMemberIds() != null && !dto.getMemberIds().isEmpty()) {
            contacts.addAll(contactRepository.findAllById(dto.getMemberIds()));
        }

        // Récupère l’utilisateur authentifié via Spring Security
        String username = authentication.getName();
        UtilisateurEntity utilisateur = userService.findByUsername(username);

        /*
         * UserDetails userDetails =
         * (UserDetails)SecurityContextHolder.getContext().getAuthentication().
         * getPrincipal();
         * String username = userDetails.getUsername();
         * DCUser user = userRepository.findByUsername(username)
         * .orElseThrow(() -> new UsernameNotFoundException("User not found"));
         */

        // Convertit les données en entité, puis sauvegarde
        ActionEntity entity = convertToEntity(dto, contacts, projet, utilisateur);
        // todo.setUtilisateur(user);
        ActionEntity savedEntity = repository.save(entity);

        return convertToDTO(savedEntity); // Retourne l’action créée
    }

    // Met à jour une action existante
    public ActionDTO update(Long id, ActionDTO dto, Authentication authentication) {
        System.out.println("Appel de la méthode update avec id = " + id);
        // Récupère l’action existante, ou lève une exception
        ActionEntity existingEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Action not found with id: " + id));

        // Met à jour les champs modifiables
        existingEntity.setTitle(dto.getTitle());
        existingEntity.setCompleted(dto.getCompleted());
        existingEntity.setDueDate(dto.getDueDate());
        existingEntity.setTextarea(dto.getTextarea());
        existingEntity.setPriorite(dto.getPriorite());

        // Met à jour les membres s’il y en a
        Set<ContactEntity> contacts = new HashSet<>();
        if (dto.getMemberIds() != null) {
            contacts.addAll(contactRepository.findAllById(dto.getMemberIds()));
        }
        existingEntity.setMembers(contacts);

        // Met à jour le projet si un nouvel ID est fourni
        if (dto.getProjetId() != null) {
            ProjetEntity projet = projetRepository.findById(dto.getProjetId())
                    .orElseThrow(
                            () -> new EntityNotFoundException("Tache introuvable avec l'id : " + dto.getProjetId()));
            existingEntity.setProjet(projet);
        }

        // Récupérer utilisateur connecté et l’assigner
        String username = authentication.getName();
        UtilisateurEntity utilisateur = userService.findByUsername(username);
        existingEntity.setUtilisateur(utilisateur);

        // Enregistre l'entité modifiée
        ActionEntity updatedEntity = repository.save(existingEntity);
        return convertToDTO(updatedEntity);
    }

    // Supprime une action selon son ID
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Action not found with id: " + id);
        }

        repository.deleteById(id); // suppression simple
    }
}
