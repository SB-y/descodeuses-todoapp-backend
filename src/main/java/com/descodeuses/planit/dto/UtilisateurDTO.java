package com.descodeuses.planit.dto;

public class UtilisateurDTO {
    private Long id;
    private String username;
    private String password;
    private String role;

    private String name;
    private String surname;
    private String genre;

    // Constructeurs
    public UtilisateurDTO() {}
    public UtilisateurDTO(Long id, String username, String password, String role,  String name, String surname, String genre) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.name = name;
        this.surname = surname;
        this.genre = genre;
    }

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