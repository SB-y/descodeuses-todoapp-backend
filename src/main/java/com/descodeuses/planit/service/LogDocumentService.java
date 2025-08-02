// Définit le package dans lequel se trouve ce service
package com.descodeuses.planit.service;

// Importe les classes Java utiles pour gérer la date et les collections
import java.time.LocalDateTime;
import java.util.Map;

// Importe les annotations Spring et les classes nécessaires
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Importe les DTO et entités nécessaires à la journalisation
import com.descodeuses.planit.dto.ActionDTO;
import com.descodeuses.planit.dto.AuthRequest;
import com.descodeuses.planit.entity.LogDocument;
import com.descodeuses.planit.entity.UtilisateurEntity;
import com.descodeuses.planit.repository.LogDocumentRepository;

// Importe les classes pour récupérer les informations sur la requête HTTP
import jakarta.servlet.http.HttpServletRequest;

// Indique que cette classe est un service Spring (composant métier)
@Service
public class LogDocumentService {

    // Injecte le repository MongoDB pour enregistrer les logs
    @Autowired
    private LogDocumentRepository repo;

    // Méthode pour enregistrer un log lié à une action utilisateur (par exemple : tâche ajoutée, modifiée...)
    public void addLog(String text, HttpServletRequest request, ActionDTO dto) {
        // Crée un nouveau document de log
        LogDocument doc = new LogDocument();

        // Définit le texte du log (ex: "Action ajoutée")
        doc.setText(text);

        // Ajoute un horodatage au log
        doc.setTimestamp(LocalDateTime.now());

        /* 
        // Prépare les informations supplémentaires à enregistrer dans le log
        Map<String, Object> extras = Map.of(
            "action", Map.of(
                "title", dto.getTitle(),
                "completed", dto.getCompleted(),
                "_class", dto.getClass().getName()
            ),
            "user", Map.of(
                "username", dto.getUsername(),
                "name", dto.getName(),
                "surname", dto.getSurname()
            )
        );
         */ 

         Map<String, Object> extras = Map.of(
            "request", Map.of(
                "action", dto
            )
        );


        // Ajoute les données supplémentaires au document de log
        doc.setExtras(extras);

        // Enregistre le log dans la base MongoDB
        repo.save(doc);
    }

    // Méthode pour enregistrer un log lié à l’authentification (login)
    public void addLog(String text, HttpServletRequest request, AuthRequest authRequest, UtilisateurEntity user) {
        // Crée un nouveau document de log
        LogDocument doc = new LogDocument();

        // Définit le texte du log (ex: "Login called")
        doc.setText(text);

        // Ajoute un horodatage au log
        doc.setTimestamp(LocalDateTime.now());

        // Prépare les informations supplémentaires (données de la requête et utilisateur)
        Map<String, Object> extras = Map.of(
            "request", Map.of(
                "username", authRequest.getUsername(),
                "_class", authRequest.getClass().getName()
            ),
            "user", Map.of(
                "name", user.getName(),
                "surname", user.getSurname(),
                "role", user.getRole()
            )
        );

        // Ajoute les données au log
        doc.setExtras(extras);

        // Enregistre le document dans MongoDB
        repo.save(doc);
    }

}