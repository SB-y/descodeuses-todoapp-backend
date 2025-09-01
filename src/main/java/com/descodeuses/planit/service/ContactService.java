// Service qui gère la logique métier pour les contacts
// - Conversion entre entités et DTO
// - Vérification que les contacts appartiennent bien à l’utilisateur connecté
// - CRUD (Create, Read, Update, Delete) pour les contacts

package com.descodeuses.planit.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.descodeuses.planit.dto.ContactDTO;
import com.descodeuses.planit.entity.ContactEntity;
import com.descodeuses.planit.entity.UtilisateurEntity;
import com.descodeuses.planit.repository.ContactRepository;

import jakarta.persistence.EntityNotFoundException;

// Indique que cette classe est un service Spring (logique métier)
@Service
public class ContactService {

    private final ContactRepository repository; // Accès à la base de données des contacts
    private final UserService userService;      // Service pour récupérer l’utilisateur connecté

    // Constructeur avec injection des dépendances
    public ContactService(ContactRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    // Conversion d’une entité Contact en DTO (pour exposer côté API)
    private ContactDTO convertToDTO(ContactEntity contact) {
        return new ContactDTO(
                contact.getId(),
                contact.getNom(),
                contact.getPrenom(),
                contact.getEmail(),
                contact.getTel());
    }

    // Conversion d’un DTO en entité Contact (pour sauvegarde en BDD)
    private ContactEntity convertToEntity(ContactDTO contactDTO, UtilisateurEntity utilisateur) {
        ContactEntity contact = new ContactEntity();
        contact.setId(contactDTO.getId());
        contact.setNom(contactDTO.getNom());
        contact.setPrenom(contactDTO.getPrenom());
        contact.setEmail(contactDTO.getEmail());
        contact.setTel(contactDTO.getTel());
        contact.setUtilisateur(utilisateur); // associe le contact à l’utilisateur connecté
        return contact;
    }

    // Récupérer tous les contacts de l’utilisateur connecté
    public List<ContactDTO> getAllByUser(Authentication authentication) {
        String username = authentication.getName(); // récupère le username de l’utilisateur connecté
        UtilisateurEntity utilisateur = userService.findByUsername(username);

        List<ContactEntity> contacts = repository.findByUtilisateur(utilisateur);

        // Conversion en DTO
        List<ContactDTO> dtos = new ArrayList<>();
        for (ContactEntity contact : contacts) {
            dtos.add(convertToDTO(contact));
        }
        return dtos;
    }

    // Récupérer un contact par son ID (uniquement si c’est bien celui de l’utilisateur connecté)
    public ContactDTO getById(Long id, Authentication authentication) {
        String username = authentication.getName();
        UtilisateurEntity utilisateur = userService.findByUsername(username);

        ContactEntity contact = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contact non trouvé avec id: " + id));

        // Vérifie que le contact appartient bien à l’utilisateur connecté
        if (!contact.getUtilisateur().getId().equals(utilisateur.getId())) {
            throw new EntityNotFoundException("Contact non trouvé pour cet utilisateur");
        }

        return convertToDTO(contact);
    }

    // Créer un nouveau contact pour l’utilisateur connecté
    public ContactDTO create(ContactDTO contactDTO, Authentication authentication) {
        String username = authentication.getName();
        UtilisateurEntity utilisateur = userService.findByUsername(username);

        ContactEntity contact = convertToEntity(contactDTO, utilisateur);
        ContactEntity savedContact = repository.save(contact);
        return convertToDTO(savedContact);
    }

    // Mettre à jour un contact existant (seulement si c’est celui de l’utilisateur connecté)
    public ContactDTO update(Long id, ContactDTO contactDTO, Authentication authentication) {
        String username = authentication.getName();
        UtilisateurEntity utilisateur = userService.findByUsername(username);

        ContactEntity existingContact = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contact non trouvé avec id: " + id));

        // Vérifie que le contact appartient bien à l’utilisateur connecté
        if (!existingContact.getUtilisateur().getId().equals(utilisateur.getId())) {
            throw new EntityNotFoundException("Contact non trouvé pour cet utilisateur");
        }

        // Mise à jour des champs
        existingContact.setNom(contactDTO.getNom());
        existingContact.setPrenom(contactDTO.getPrenom());
        existingContact.setEmail(contactDTO.getEmail());
        existingContact.setTel(contactDTO.getTel());

        ContactEntity updatedContact = repository.save(existingContact);
        return convertToDTO(updatedContact);
    }

    // Supprimer un contact (uniquement si c’est celui de l’utilisateur connecté)
    public void delete(Long id, Authentication authentication) {
        String username = authentication.getName();
        UtilisateurEntity utilisateur = userService.findByUsername(username);

        ContactEntity contact = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contact non trouvé avec id: " + id));

        // Vérifie que le contact appartient bien à l’utilisateur connecté
        if (!contact.getUtilisateur().getId().equals(utilisateur.getId())) {
            throw new EntityNotFoundException("Contact non trouvé pour cet utilisateur");
        }

        repository.deleteById(id);
    }
}
