package com.descodeuses.planit.controller;

import org.springframework.web.bind.annotation.*;

import com.descodeuses.planit.dto.ContactDTO;
import com.descodeuses.planit.service.ContactService;


import java.util.List;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

@RestController 
@RequestMapping("api/contact") 
public class ContactController {

private final ContactService service; // variable service ne pas être utilisée en dehors de controller pour des questions de sécurité

    public ContactController(ContactService service) {
        this.service = service;
    }


    @GetMapping("/{id}")
    public ResponseEntity<ContactDTO> getById(@PathVariable Long id) {
        ContactDTO contactDTO = service.getById(id);
        return new ResponseEntity<>(contactDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ContactDTO>> getAll() {
        List<ContactDTO> items = service.getAll();
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ContactDTO> create(@RequestBody ContactDTO dto) {
        ContactDTO created = service.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContactDTO> update(@PathVariable Long id, @RequestBody ContactDTO dto) {
        ContactDTO updated = service.update(id, dto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}


















/*package com.descodeuses.planit.controller; // 📁 Le package de ce contrôleur

// 🌐 Gère les annotations liées aux routes HTTP (@GetMapping, @PostMapping, etc.)
import org.springframework.web.bind.annotation.*;

import com.descodeuses.planit.entity.Contact;
import com.descodeuses.planit.repository.ContactRepository;

// 🧰 Liste de données (ici : liste de contacts)
import java.util.List;

// 🧩 Optional : représente une valeur qui peut être présente ou non (évite les null)
import java.util.Optional;

// 🔧 Permet l'injection automatique de dépendances (ex : repository)
import org.springframework.beans.factory.annotation.Autowired;

// 📤 Permet de renvoyer une réponse HTTP avec un code et éventuellement un corps
// → très utile pour renvoyer 200 OK, 404 Not Found, 201 Created, etc.
import org.springframework.http.ResponseEntity;

@RestController // 🎯 Déclare cette classe comme un contrôleur REST
@RequestMapping("api/contact") // 📍 Préfixe des routes : /api/contact/...
public class ContactController {

    @Autowired // 🔌 Injecte automatiquement le ContactRepository (plus besoin de l’instancier)
    private ContactRepository contactRepository;

    // 🔎 GET : renvoie tous les contacts (ex: GET /api/contact)
    @GetMapping()
    public List<Contact> getContacts() {
        return contactRepository.findAll(); // 📡 Retourne la liste de tous les contacts en base
    }

    // 🔎 GET : renvoie un contact par ID (ex: GET /api/contact/3)
    @GetMapping("/{id}")
    public ResponseEntity<Contact> getContactById(@PathVariable Long id) {
        Optional<Contact> contact = contactRepository.findById(id); // 🔍 Peut contenir un contact ou être vide

        // ✅ Si trouvé : 200 OK avec l'objet
        // ❌ Sinon : 404 Not Found
        return contact.map(ResponseEntity::ok) // 🟢 map() transforme le Optional en réponse OK avec l’objet
                      .orElseGet(() -> ResponseEntity.notFound().build()); // 🔴 Sinon, 404
    }

    // ➕ POST : créer un nouveau contact (ex: POST /api/contact)
    @PostMapping
    public Contact postContact(@RequestBody Contact contact) {
        return contactRepository.save(contact); // 💾 Enregistre et retourne le nouveau contact
    }

    // ✏️ PATCH : met à jour un contact (ex: PATCH /api/contact/1)
    @PutMapping("/{id}")
    public ResponseEntity<Contact> updateContact(@PathVariable Long id, @RequestBody Contact updated) {
        return contactRepository.findById(id).map(contact -> {
            // 📝 Met à jour les champs si contact trouvé
            contact.setNom(updated.getNom());
            contact.setPrenom(updated.getPrenom());
            contact.setEmail(updated.getEmail());
            contact.setTel(updated.getTel());

            // 💾 Sauvegarde en base et retourne l’objet mis à jour
            return ResponseEntity.ok(contactRepository.save(contact));
        }).orElse(ResponseEntity.notFound().build()); // ❌ Si pas trouvé → 404
    }

    // ❌ DELETE : supprime un contact (ex: DELETE /api/contact/1)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        if (contactRepository.existsById(id)) {
            contactRepository.deleteById(id); // 🗑️ Suppression
            return ResponseEntity.noContent().build(); // ✅ 204 No Content
        }
        return ResponseEntity.notFound().build(); // ❌ 404 Not Found si ID inconnu
    }
}

*/





/* 
    // 🗂️ Simule une "base de données" en mémoire
    List<Contact> listContacts = new ArrayList<>();

    // 🔁 Constructeur : on remplit la liste une seule fois au lancement
    public ContactController() {
        listContacts.add(new Contact(1L, "prenom 1", "nom 1", "prenom1@ex.com"));
        listContacts.add(new Contact(2L, "prenom 2", "nom 2", "prenom2@ex.com"));
        listContacts.add(new Contact(3L, "prenom 3", "nom 3", "prenom3@ex.com"));
    }

    // 🔎 GET : renvoie tous les contacts
    @GetMapping()
    public List<Contact> getContacts() {
        return listContacts;
    }

    // 🔎 GET : renvoie un contact par ID (simulé)
    @GetMapping("/{id}")
    public ResponseEntity<Contact> getContactById(@PathVariable Long id) {
        for (Contact contact : listContacts) {
            if (contact.getId().equals(id)) {
                return new ResponseEntity<>(contact, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // ➕ POST : simulation simple
    @PostMapping
    public String postContacts() {
        return "post";
    }

    // ✏️ PUT : simulation simple
    @PutMapping
    public String putContacts() {
        return "put";
    }

    // ❌ DELETE : simulation simple
    @DeleteMapping
    public String deleteContacts() {
        return "del";
    }
}

*/