package com.descodeuses.planit.dto;


import jakarta.validation.constraints.NotBlank;


public class RegisterRequest {

    @NotBlank(message = "Le username est obligatoire")
    private String username;

    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;

    @NotBlank(message = "Le prénom est obligatoire")
    private String name;

    @NotBlank(message = "Le nom est obligatoire")
    private String surname;

    @NotBlank(message = "L'email est obligatoire")
    private String email;

    private String genre;

    // Constructeur vide obligatoire pour Spring
    public RegisterRequest() {
    }

    // Constructeur complet
    public RegisterRequest(String username, String password, String name, 
                           String surname, String email, String genre) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.genre = genre;
    }

    // Getters & Setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    // ⚠️ Pas de toString avec password !
    public void setPassword(String password) {
        this.password = password;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}