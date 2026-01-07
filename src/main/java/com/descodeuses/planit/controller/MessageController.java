package com.descodeuses.planit.controller;

import com.descodeuses.planit.dto.MessageDTO;
import com.descodeuses.planit.service.MessageService;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin("*")
public class MessageController {

    private final MessageService service;

    // Injection du service via constructeur
    public MessageController(MessageService service) {
        this.service = service;
    }

    // Récupérer les messages d'une tâche donnée
    @GetMapping("/{todoId}")
    public List<MessageDTO> getMessages(@PathVariable Long todoId, Authentication auth) {
        // auth = informations de l’utilisateur connecté
        return service.getMessages(todoId, auth);
    }

    // Ajouter un message à une tâche
    @PostMapping("/{todoId}")
    public MessageDTO addMessage(@PathVariable Long todoId,
                                 @RequestBody String content,
                                 Authentication auth) {
        // auth = permet d'identifier qui envoie le message
        return service.addMessage(todoId, content, auth);
    }
}
