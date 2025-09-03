package com.descodeuses.planit.dto;

import java.time.LocalDate;
import java.util.Set;

public class ActionDTO {

    private Long id;
    private String title;
    private boolean completed;
    private LocalDate dueDate;
    private String textarea;
    private Integer priorite;

    // Champ utilisé à la réception côté backend (POST/PUT) :
    // contient uniquement les IDs des contacts sélectionnés
    private Set<Long> memberIds;

    // Champ utilisé à l'envoi vers le frontend (GET) :
    // contient les objets complets ContactDTO
    private Set<ContactDTO> membres;

    // Utilisateurs assignés
    private Set<Long> assignedUserIds;          // IDs des utilisateurs assignés (POST/PUT)
    private Set<UtilisateurDTO> utilisateursAssignes; // Détails utilisateurs assignés (GET)

    private Long projetId;
    private ProjetDTO projet;

    private Long utilisateurId;
    private String name;     
    private String surname;  
    private String genre;
    private String username;


    // Mise à jour du constructeur
    public ActionDTO(Long id, String title, boolean completed, LocalDate dueDate, String textarea, Integer priorite) {
        this.id = id;
        this.title = title;
        this.completed = completed;
        this.dueDate = dueDate;
        this.textarea = textarea;
        this.priorite = priorite;
    }

    // Getters et Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public boolean getCompleted() {
        return this.completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

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

    // Utilisé uniquement pour l’envoi de la réponse GET : permet d'afficher les membres dans Angular
    public Set<ContactDTO> getMembres() {
        return membres;
    }

    public void setMembres(Set<ContactDTO> membres) {
        this.membres = membres;
    }

    // Utilisé uniquement en entrée (POST/PUT) : on envoie les IDs des membres choisis
    public Set<Long> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(Set<Long> memberIds) {
        this.memberIds = memberIds;
    }



    public Set<Long> getAssignedUserIds() 
    { return assignedUserIds; }

    public void setAssignedUserIds(Set<Long> assignedUserIds) 
    { this.assignedUserIds = assignedUserIds; }

    public Set<UtilisateurDTO> getUtilisateursAssignes() 
    { return utilisateursAssignes; }

    public void setUtilisateursAssignes(Set<UtilisateurDTO> utilisateursAssignes) 
    { this.utilisateursAssignes = utilisateursAssignes; }




    
    public Long getProjetId() {
        return projetId;
    }

    public void setProjetId(Long projetId) {
        this.projetId = projetId;
    }

    public ProjetDTO getProjet() {
        return projet;
    }

    public void setProjet(ProjetDTO projet) {
        this.projet = projet;
    }


    public Long getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(Long utilisateurId) {
        this.utilisateurId = utilisateurId;
    }



    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
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