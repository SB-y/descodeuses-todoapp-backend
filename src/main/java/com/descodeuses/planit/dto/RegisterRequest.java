/*
 * Rôle :
 * Ce DTO est utilisé pour recevoir les informations nécessaires
 * à la création d’un nouvel utilisateur lors de l’inscription.

 * Sécurité :
 * - Le mot de passe est reçu en clair uniquement côté backend.
 * - Il est immédiatement encodé avant stockage.
 * - Il n’est jamais renvoyé dans une réponse API.
*/


package com.descodeuses.planit.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public class RegisterRequest {

    @NotBlank(message = "Le username est obligatoire")
    @Email
    private String username;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 5)
    private String password;

    @NotBlank(message = "Le prénom est obligatoire")
    private String name;

    @NotBlank(message = "Le nom est obligatoire")
    private String surname;

    private String genre;

    // Constructeur vide obligatoire pour Spring
    public RegisterRequest() {
    }

    // Constructeur complet
    public RegisterRequest(String username, String password, String name, 
                           String surname, String genre) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
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


    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}