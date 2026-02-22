/*
 * Rôle :
 * Ce DTO est utilisé pour renvoyer les informations d’authentification
 * au frontend après une connexion réussie.

 * Contenu :
 * - token : JWT généré après authentification valide.
 * - role  : rôle de l’utilisateur (ex : ROLE_USER, ROLE_ADMIN).
 * 
 * * Points de sécurité importants :
 * - Aucun mot de passe n’est renvoyé.
 * - Le JWT contient les rôles et l’identité signés.
 * - Le token est vérifié à chaque requête via JwtFilter.

*/


package com.descodeuses.planit.dto;

public class AuthResponse {

    private String token;
    private String role;

    public AuthResponse(String token, String role) {
        this.token = token;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
