// Déclare que cette classe fait partie du package com.descodeuses.planit.entity
package com.descodeuses.planit.entity;

import java.util.HashSet;
import java.util.Set;

// Import des annotations JPA pour définir une entité de base de données
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

// Indique que cette classe est une entité JPA (table en base de données)
@Entity

// Spécifie le nom de la table dans la base : "utilisateur"
@Table(name = "utilisateur")
public class UtilisateurEntity {

    // Clé primaire de l'entité (colonne `id`)
    @Id

    // Génération automatique de l'ID par la base (auto-incrémentée)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relation 1-n : un utilisateur peut avoir plusieurs actions
    @OneToMany(mappedBy = "utilisateur")
    private Set<ActionEntity> actions = new HashSet<>();

    // Getter pour récupérer la liste des actions liées à cet utilisateur
    public Set<ActionEntity> getActions() {
        return actions;
    }

    // Setter pour associer une liste d’actions à cet utilisateur
    public void setActions(Set<ActionEntity> actions) {
        this.actions = actions;
    }

    // Champ pour le nom d’utilisateur (utilisé pour l’authentification)
    private String username;

    // Mot de passe hashé
    private String password;

    // Rôle de l’utilisateur (ex : "ADMIN", "USER")
    private String role;

    private String name; // ou prenom
    private String surname; // ou nom
    private String genre;

    // Getter et setter pour `id`
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getter et setter pour `username`
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getter et setter pour `password`
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getter et setter pour `role`
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}