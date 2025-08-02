// Déclare le package dans lequel se trouve cette interface
package com.descodeuses.planit.repository;

// Importe la classe Optional (pour gérer les valeurs pouvant être nulles)
import java.util.Optional;

// Importe JpaRepository (interface Spring Data JPA)
import org.springframework.data.jpa.repository.JpaRepository;

// Permet à Spring de détecter cette interface comme un composant Repository
import org.springframework.stereotype.Repository;

// Importe l’entité représentant un utilisateur dans la base de données
import com.descodeuses.planit.entity.UtilisateurEntity;

// Marque l’interface comme un bean Spring, permettant l’injection et l’automatisation
@Repository
public interface UtilisateurRepository extends JpaRepository<UtilisateurEntity, Long> {
    
    // Méthode personnalisée : permet de rechercher un utilisateur par son username
    // Spring Data JPA génère automatiquement la requête correspondante
    Optional<UtilisateurEntity> findByUsername(String username);
}