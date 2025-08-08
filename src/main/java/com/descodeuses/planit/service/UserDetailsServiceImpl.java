// implémentation spécifique de l’interface Spring Security UserDetailsService.
// rôle unique : charger les informations utilisateur à partir de la base de données 
// quand Spring Security en a besoin (typiquement lors de l’authentification).
// renvoie un objet UserDetails (avec username, password, roles) qui permet à Spring Security 
//de vérifier les identifiants.


package com.descodeuses.planit.service;

// Import des classes nécessaires
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired; // Pour l'injection de dépendances
import org.springframework.security.core.authority.SimpleGrantedAuthority; // Pour représenter les rôles
import org.springframework.security.core.userdetails.User; // Implémentation standard de UserDetails
import org.springframework.security.core.userdetails.UserDetails; // Interface que Spring utilise pour l'authentification
import org.springframework.security.core.userdetails.UserDetailsService; // Interface à implémenter pour la logique de chargement des utilisateurs
import org.springframework.security.core.userdetails.UsernameNotFoundException; // Exception levée si l'utilisateur n'existe pas
import org.springframework.stereotype.Service; // Pour marquer cette classe comme un service Spring

import com.descodeuses.planit.entity.UtilisateurEntity; // Ton entité représentant un utilisateur dans ta base de données
import com.descodeuses.planit.repository.UtilisateurRepository; // Le repository pour accéder aux utilisateurs

// Classe de service Spring pour la gestion de l’authentification des utilisateurs
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    // Injection du repository permettant d'accéder à la base de données des utilisateurs
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    // Méthode obligatoire à implémenter : permet à Spring Security de charger un utilisateur via son username
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // On cherche l'utilisateur dans la base par son username (login)
        // Si non trouvé, on lève une exception
        UtilisateurEntity user = utilisateurRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // On retourne un objet Spring Security `User` (qui implémente UserDetails)
        // Il contient : le nom d’utilisateur, le mot de passe, et une liste de rôles (authorities)
        return new User(
            user.getUsername(), // identifiant
            user.getPassword(), // mot de passe (doit être déjà encodé)
            List.of(new SimpleGrantedAuthority(user.getRole())) // autorité/role ex: ROLE_ADMIN
        );
    }
}