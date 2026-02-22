/*Ce DTO est utilisé pour transporter les identifiants de connexion
 (username + password) lors d’une tentative d’authentification.

 Ce DTO est uniquement utilisé en entrée (request).
  Le mot de passe n’est jamais renvoyé dans une réponse API.
  Le mot de passe n’est jamais stocké en clair en base.
  La vérification est faite via PasswordEncoder (BCrypt).
*/




package com.descodeuses.planit.dto;

public class AuthRequest {

    private String username;
    private String password;

    public AuthRequest(String username, String password) {
        this.username = username;
        this.password = password;

    }

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


}