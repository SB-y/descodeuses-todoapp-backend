package com.descodeuses.planit.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="contact")

public class ContactEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;
    private String prenom;
    private String email;
    private String tel;

    // Ajout de la relation ManyToOne vers UtilisateurEntity
    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private UtilisateurEntity utilisateur;



    public ContactEntity() {} // Constructeur vide requis par JPA

    // Constructeur avec tous les champs
    public ContactEntity(Long id, String nom, String prenom, String email, String tel) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.tel = tel;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }


    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }


    public UtilisateurEntity getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(UtilisateurEntity utilisateur) {
        this.utilisateur = utilisateur;
    }


    @ManyToMany(mappedBy = "members")
    private Set<ActionEntity> todos = new HashSet<>();

    public Set<ActionEntity> getTodos() {
        return todos;
    }

    public void setTodos(Set<ActionEntity> todos) {
        this.todos = todos;
    }
}

