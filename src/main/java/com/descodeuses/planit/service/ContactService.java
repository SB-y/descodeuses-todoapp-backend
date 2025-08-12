package com.descodeuses.planit.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.descodeuses.planit.dto.ActionDTO;
import com.descodeuses.planit.dto.ContactDTO;
import com.descodeuses.planit.entity.ContactEntity;
import com.descodeuses.planit.entity.UtilisateurEntity;
import com.descodeuses.planit.repository.ContactRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ContactService {

    private final ContactRepository repository;
    private final UserService userService;

    public ContactService(ContactRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    private ContactDTO convertToDTO(ContactEntity contact) {
        return new ContactDTO(
                contact.getId(),
                contact.getNom(),
                contact.getPrenom(),
                contact.getEmail(),
                contact.getTel());
    }

    private ContactEntity convertToEntity(ContactDTO contactDTO, UtilisateurEntity utilisateur) {
        ContactEntity contact = new ContactEntity();
        contact.setId(contactDTO.getId());
        contact.setNom(contactDTO.getNom());
        contact.setPrenom(contactDTO.getPrenom());
        contact.setEmail(contactDTO.getEmail());
        contact.setTel(contactDTO.getTel());
        contact.setUtilisateur(utilisateur);
        return contact;
    }

    // Récupérer tous les contacts de l'utilisateur connecté
    public List<ContactDTO> getAllByUser(Authentication authentication) {
        String username = authentication.getName();
        UtilisateurEntity utilisateur = userService.findByUsername(username);

        List<ContactEntity> contacts = repository.findByUtilisateur(utilisateur);

        List<ContactDTO> dtos = new ArrayList<>();
        for (ContactEntity contact : contacts) {
            dtos.add(convertToDTO(contact));
        }
        return dtos;
    }

    public ContactDTO getById(Long id, Authentication authentication) {
        String username = authentication.getName();
        UtilisateurEntity utilisateur = userService.findByUsername(username);

        ContactEntity contact = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contact non trouvé avec id: " + id));

        // Vérifier que le contact appartient bien à l'utilisateur connecté
        if (!contact.getUtilisateur().getId().equals(utilisateur.getId())) {
            throw new EntityNotFoundException("Contact non trouvé pour cet utilisateur");
        }

        return convertToDTO(contact);
    }

    public ContactDTO create(ContactDTO contactDTO, Authentication authentication) {
        String username = authentication.getName();
        UtilisateurEntity utilisateur = userService.findByUsername(username);

        ContactEntity contact = convertToEntity(contactDTO, utilisateur);
        ContactEntity savedContact = repository.save(contact);
        return convertToDTO(savedContact);
    }

    public ContactDTO update(Long id, ContactDTO contactDTO, Authentication authentication) {
        String username = authentication.getName();
        UtilisateurEntity utilisateur = userService.findByUsername(username);

        ContactEntity existingContact = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contact non trouvé avec id: " + id));

        // Vérifier que le contact appartient bien à l'utilisateur connecté
        if (!existingContact.getUtilisateur().getId().equals(utilisateur.getId())) {
            throw new EntityNotFoundException("Contact non trouvé pour cet utilisateur");
        }

        existingContact.setNom(contactDTO.getNom());
        existingContact.setPrenom(contactDTO.getPrenom());
        existingContact.setEmail(contactDTO.getEmail());
        existingContact.setTel(contactDTO.getTel());

        ContactEntity updatedContact = repository.save(existingContact);
        return convertToDTO(updatedContact);
    }

    public void delete(Long id, Authentication authentication) {
        String username = authentication.getName();
        UtilisateurEntity utilisateur = userService.findByUsername(username);

        ContactEntity contact = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contact non trouvé avec id: " + id));

        // Vérifier que le contact appartient bien à l'utilisateur connecté
        if (!contact.getUtilisateur().getId().equals(utilisateur.getId())) {
            throw new EntityNotFoundException("Contact non trouvé pour cet utilisateur");
        }

        repository.deleteById(id);
    }
}