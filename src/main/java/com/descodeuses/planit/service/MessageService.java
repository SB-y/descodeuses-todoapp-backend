package com.descodeuses.planit.service;

import com.descodeuses.planit.dto.MessageDTO;
import com.descodeuses.planit.entity.*;
import com.descodeuses.planit.repository.MessageRepository;
import com.descodeuses.planit.repository.ActionRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    // Repository pour accéder aux messages (CRUD)
    private final MessageRepository messageRepo;

    // Repository pour récupérer la tâche (todo) associée
    private final ActionRepository actionRepo;

    // Service pour récupérer l'utilisateur connecté via son username
    private final UserService userService;


    // Injection des dépendances via constructeur
    public MessageService(MessageRepository messageRepo, ActionRepository actionRepo, UserService userService) {
        this.messageRepo = messageRepo;
        this.actionRepo = actionRepo;
        this.userService = userService;
    }


    //  Vérification des droits

    /**
     * Vérifie si l'utilisateur connecté est autorisé à voir/écrire les messages :
     * - propriétaire de la tâche
     * - OU utilisateur assigné
     */
    private void checkAccess(ActionEntity todo, UtilisateurEntity user) {

        boolean isOwner = todo.getUtilisateur().getId().equals(user.getId());

        boolean isAssigned = todo.getUtilisateursAssignes()
                .stream()
                .anyMatch(u -> u.getId().equals(user.getId()));

        if (!isOwner && !isAssigned) {
            throw new SecurityException("Vous n'êtes pas autorisé à accéder aux messages de cette tâche.");
        }
    }



        //        AJOUT MESSAGE
    /**
     * Enregistre un nouveau message dans la messagerie de la tâche.
     */
    public MessageDTO addMessage(Long todoId, String content, Authentication auth) {

        // Identifie l'utilisateur connecté
        UtilisateurEntity user = userService.findByUsername(auth.getName());

        // Récupère la tâche concernée
        ActionEntity todo = actionRepo.findById(todoId)
                .orElseThrow(() -> new EntityNotFoundException("Tâche introuvable"));

        // Vérifie qu'il a le droit de poster
        checkAccess(todo, user);

        // On crée une nouvelle entité MessageEntity
        MessageEntity m = new MessageEntity();
        m.setAuthor(user);      // auteur = user connecté
        m.setTodo(todo);        // associé à la tâche
        m.setContent(content);  // contenu du message

        // Enregistrement du message
        MessageEntity saved = messageRepo.save(m);

        // On renvoie un DTO du message pour le frontend
        return convertToDTO(saved);
    }

    

  //       LECTURE DES MESSAGES
    /**
     * Récupère les messages d'une tâche, triés par date croissante.
     */
    public List<MessageDTO> getMessages(Long todoId, Authentication auth) {

        // Identifie l'utilisateur connecté
        UtilisateurEntity user = userService.findByUsername(auth.getName());

        // Récupère la tâche
        ActionEntity todo = actionRepo.findById(todoId)
                .orElseThrow(() -> new EntityNotFoundException("Tâche introuvable"));

        // Vérifie les droits d'accès
        checkAccess(todo, user);

        // Récupère les messages et les convertit en DTO
        return messageRepo.findByTodoOrderByCreatedAtAsc(todo)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }



    //  Conversion Entity -> DTO

    /**
     * Convertit une entité MessageEntity en MessageDTO.
     * On renvoie un DTO car :
     * - On ne veut jamais exposer les entités JPA directement à Angular
     * - On contrôle exactement les données envoyées au frontend
     */
    private MessageDTO convertToDTO(MessageEntity m) {
        return new MessageDTO(
                m.getId(),                     // ID du message
                m.getTodo().getId(),           // ID de la tâche associée
                m.getAuthor().getId(),         // ID de l'auteur
                m.getAuthor().getSurname(),    // Prénom
                m.getAuthor().getName(),       // Nom
                m.getContent(),                // Texte du message
                m.getCreatedAt()               // Date d'envoi
        );
    }



}
