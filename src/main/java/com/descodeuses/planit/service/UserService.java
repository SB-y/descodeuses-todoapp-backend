// Déclare que cette classe fait partie du package com.descodeuses.planit.service
package com.descodeuses.planit.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
// Indique que c’est un service Spring, donc une classe métier réutilisable
import org.springframework.stereotype.Service;

// Import de l’entité Utilisateur (celle qui correspond à la table des utilisateurs)
import com.descodeuses.planit.entity.UtilisateurEntity;

// Import du repository (interface permettant d'accéder aux utilisateurs en base)
import com.descodeuses.planit.repository.UtilisateurRepository;

// Exception lancée si l’utilisateur n’est pas trouvé
import jakarta.persistence.EntityNotFoundException;

// Indique à Spring que cette classe est un service (composant de la couche métier)
@Service
public class UserService {

    // Attribut privé pour accéder aux méthodes du repository
    private final UtilisateurRepository repository;

    // Constructeur avec injection du repository (Spring s’en occupe)
    public UserService(UtilisateurRepository repository) {
        this.repository = repository;
    }

    // Méthode publique pour rechercher un utilisateur par son nom d'utilisateur (username)
    public UtilisateurEntity findByUsername(String username) {
        // Recherche dans la base via le repository ; si rien trouvé, lève une exception claire
        return repository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable avec le nom : " + username));
    }


    // Méthode publique pour avoir tous les utilisateurs
    public List<UtilisateurEntity> getAllUtilisateurs() {
        return repository.findAll();
    }

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    // Méthode publique pour avoir l'utilisateur actuellement connecté
    public UtilisateurEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return utilisateurRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));
    }

}