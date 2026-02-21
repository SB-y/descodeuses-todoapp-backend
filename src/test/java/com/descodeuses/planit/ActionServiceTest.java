package com.descodeuses.planit;

// Imports statiques pour les assertions et Mockito
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

// JUnit & Mockito
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

// Imports du projet
import com.descodeuses.planit.dto.ActionDTO;
import com.descodeuses.planit.entity.ActionEntity;
import com.descodeuses.planit.repository.ActionRepository;
import com.descodeuses.planit.repository.ContactRepository;
import com.descodeuses.planit.repository.ProjetRepository;
import com.descodeuses.planit.repository.UtilisateurRepository;
import com.descodeuses.planit.service.ActionService;
import com.descodeuses.planit.service.UserService;

/**
 * Test unitaire du service ActionService.
 * 
 * Ce test utilise Mockito pour simuler les dépendances
 * (repositories et services).
 * 
 * On ne démarre PAS le contexte Spring complet.
 */
@ExtendWith(MockitoExtension.class)
class ActionServiceTest {

    // Dépendances simulées (mockées)
    @Mock
    private ActionRepository repository;

    @Mock
    private ContactRepository contactRepository;

    @Mock
    private ProjetRepository projetRepository;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private UserService userService;

    /**
     * Service testé.
     * Mockito injecte automatiquement les mocks ci-dessus
     * dans le constructeur de ActionService.
     */
    @InjectMocks
    private ActionService actionService;

    /**
     * Test métier :
     * Vérifie qu'un utilisateur propriétaire peut modifier
     * le titre d'une tâche via la méthode update().
     */
    @Test
    void shouldUpdateTitleWhenUserIsOwner() {

        // =========================
        // GIVEN (préparation)
        // =========================

        Long actionId = 1L;
        String username = "sarah";

        // Tâche existante en base
        ActionEntity existing = new ActionEntity();
        existing.setId(actionId);
        existing.setTitle("Old Title");

        // DTO reçu depuis le front (nouveau titre)
        ActionDTO dto = new ActionDTO(actionId, username, false, null, username, null);
        dto.setTitle("New Title");
        dto.setCompleted(false);

        // Simulation de l'utilisateur authentifié
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);

        // Simulation : la tâche appartient bien à l'utilisateur
        when(repository.findByIdAndUtilisateurUsername(actionId, username))
                .thenReturn(Optional.of(existing));

        // Simulation : sauvegarde retourne l'entité mise à jour
        when(repository.save(existing)).thenReturn(existing);

        // =========================
        // WHEN (exécution)
        // =========================

        ActionDTO result = actionService.update(actionId, dto, authentication);

        // =========================
        // THEN (vérification)
        // =========================

        // Le titre doit être modifié
        assertEquals("New Title", result.getTitle());

        // Vérifie que la sauvegarde a bien été appelée
        verify(repository).save(existing);
    }
}
