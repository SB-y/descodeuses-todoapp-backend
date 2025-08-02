package com.descodeuses.planit.dto;

public class ContactDTO {

    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String tel;

    public ContactDTO(Long id, String prenom, String nom, String email, String tel) {
        this.id = id;
        this.prenom = prenom;
        this.nom = nom;
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

}
