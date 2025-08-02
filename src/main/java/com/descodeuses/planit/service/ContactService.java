package com.descodeuses.planit.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.descodeuses.planit.dto.ContactDTO;
import com.descodeuses.planit.entity.ContactEntity;
import com.descodeuses.planit.repository.ContactRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ContactService {

    private final ContactRepository repository;

    public ContactService(ContactRepository repository) {
        this.repository = repository;
    }

    private ContactDTO convertToDTO(ContactEntity contact) {
        return new ContactDTO(
                contact.getId(),
                contact.getNom(),
                contact.getPrenom(),
                contact.getEmail(),
                contact.getTel());

    }

    private ContactEntity convertToEntity(ContactDTO contactDTO) {

        ContactEntity contact = new ContactEntity();
        contact.setId(contactDTO.getId());
        contact.setNom(contactDTO.getNom());
        contact.setPrenom(contactDTO.getPrenom());
        contact.setTel(contactDTO.getTel());
        contact.setEmail(contactDTO.getEmail());

        return contact;

    }

    public List<ContactDTO> getAll() {
        List<ContactEntity> entities = repository.findAll();

        List<ContactDTO> dtos = new ArrayList<>();

        for (ContactEntity item : entities) {
            dtos.add(convertToDTO(item));
        }

        return dtos;
    }

    public ContactDTO getById(Long id) {
        Optional<ContactEntity> contact = repository.findById(id);

        if (contact.isEmpty()) {
            throw new EntityNotFoundException("Contact non trouvé avec id: " + id);
        }

        return convertToDTO(contact.get());

    }

    public ContactDTO create(ContactDTO contactDTO) {
        ContactEntity contact = convertToEntity(contactDTO);
        ContactEntity savedContact = repository.save(contact);
        return convertToDTO(savedContact);
    }

    public ContactDTO update(Long id, ContactDTO contactDTO) {
        ContactEntity existingContact = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contact non trouvé avec id: " + id));

        existingContact.setNom(contactDTO.getNom());
        existingContact.setPrenom(contactDTO.getPrenom());
        existingContact.setTel(contactDTO.getTel());
        existingContact.setEmail(contactDTO.getEmail());

        ContactEntity updatedContact = repository.save(existingContact);
        return convertToDTO(updatedContact);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Contact non trouvé avec id: " + id);
        }
        repository.deleteById(id);
    }

}
