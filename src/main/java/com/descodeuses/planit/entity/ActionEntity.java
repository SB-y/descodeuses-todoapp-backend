// Déclaration du package dans lequel se trouve la classe
package com.descodeuses.planit.entity;

// Importation de la classe LocalDate pour gérer les dates sans heure
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

// Importation des annotations JPA pour la persistance des données
import jakarta.persistence.Column; // Permet de configurer les colonnes d'une table
import jakarta.persistence.Entity; // Indique que cette classe est une entité JPA (liée à une table de BDD)
import jakarta.persistence.GeneratedValue; // Permet de générer automatiquement la valeur de l'identifiant
import jakarta.persistence.GenerationType; // Définit le type de stratégie de génération d'ID
import jakarta.persistence.Id; // Indique le champ clé primaire (identifiant unique)
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table; // Permet de donner un nom spécifique à la table en base de données

// Annotation JPA : cette classe sera une entité (une table)
@Entity

// Annotation JPA : le nom de la table sera "todo" dans la base de données
@Table(name = "todo") // table créée à travers la classe Action

public class ActionEntity {

    // Clé primaire (ID) de l'entité
    @Id
    // Génération automatique de l'ID selon la stratégie "IDENTITY" (auto-incrément
    // en BDD)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Colonne "title" ne peut pas être null (obligatoire)
    @Column(nullable = false)

    @ManyToMany
    @JoinTable(name = "todo_contact", joinColumns = @JoinColumn(name = "todo_id"), inverseJoinColumns = @JoinColumn(name = "contact_id"))

    // Attribut de l'entité `Action` qui contient tous les contacts (membres)
    // associés à cette action.
    // Chaque membre est une instance de `ContactEntity`, ce qui signifie que ce
    // champ représente une relation vers d'autres entités.
    private Set<ContactEntity> members = new HashSet<>();

    // Getter classique pour accéder à la liste des membres associés à une
    // action.
    // Cela permet par exemple, dans le service, de faire : `action.getMembers()`
    public Set<ContactEntity> getMembers() {
        return members;
    }

    // Setter classique pour définir les membres associés à une action.
    // Typiquement utilisé lors de la création ou mise à jour d'une action,
    // quand on convertit un DTO reçu contenant les `memberIds` en `ContactEntity` à
    // partir de la base.
    public void setMembers(Set<ContactEntity> members) {
        this.members = members;
    }

    // pour la relation plusieurs taches pour un projet
    @ManyToOne(optional = true)
    @JoinColumn(name = "projet_id", nullable = true)
    private ProjetEntity projet;

    // pour la relation un user pour plusieurs taches
    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private UtilisateurEntity utilisateur;

    private String title;

    // Colonne booléenne indiquant si l'action est terminée ou non
    private boolean completed;

    // Colonne contenant la date limite pour accomplir l'action
    private LocalDate dueDate;

    private String textarea;
    private Integer priorite;

    // Getter pour l'ID (obligatoire pour accéder à la valeur du champ privé)
    public Long getId() {
        return id;
    }

    // Setter pour l'ID (permet de modifier la valeur)
    public void setId(Long id) {
        this.id = id;
    }

    // Getter pour le titre
    public String getTitle() {
        return title;
    }

    // Setter pour le titre
    public void setTitle(String title) {
        this.title = title;
    }

    // Getter pour "completed" (vérifie si l'action est faite)
    public boolean isCompleted() {
        return completed;
    }

    public boolean getCompleted() {
        return this.completed;
    }

    // Setter pour "completed"
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    // Getter pour la date d’échéance
    public LocalDate getDueDate() {
        return dueDate;
    }

    // Setter pour la date d’échéance
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getTextarea() {
        return textarea;
    }

    public void setTextarea(String textarea) {
        this.textarea = textarea;
    }

    public Integer getPriorite() {
        return priorite;
    }

    public void setPriorite(Integer priorite) {
        this.priorite = priorite;
    }

    public ProjetEntity getProjet() {
        return projet;
    }

    public void setProjet(ProjetEntity projet) {
        this.projet = projet;
    }

    public UtilisateurEntity getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(UtilisateurEntity utilisateur) {
        this.utilisateur = utilisateur;
    }

    // Nouvel attribut : utilisateurs assignés à la tâche
    @ManyToMany
    @JoinTable(name = "todo_utilisateur_assigne", joinColumns = @JoinColumn(name = "todo_id"), inverseJoinColumns = @JoinColumn(name = "utilisateur_id"))
    private Set<UtilisateurEntity> utilisateursAssignes = new HashSet<>();

    public Set<UtilisateurEntity> getUtilisateursAssignes() {
        return utilisateursAssignes;
    }

    public void setUtilisateursAssignes(Set<UtilisateurEntity> utilisateursAssignes) {
        this.utilisateursAssignes = utilisateursAssignes;
    }

}
