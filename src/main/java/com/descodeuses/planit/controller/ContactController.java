package com.descodeuses.planit.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.descodeuses.planit.dto.ContactDTO;
import com.descodeuses.planit.service.ContactService;

@RestController 
@RequestMapping("api/contact") 
public class ContactController {

    private final ContactService service;

    public ContactController(ContactService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactDTO> getById(@PathVariable Long id, Authentication authentication) {
        ContactDTO contactDTO = service.getById(id, authentication);
        return new ResponseEntity<>(contactDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ContactDTO>> getAll(Authentication authentication) {
        List<ContactDTO> items = service.getAllByUser(authentication);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ContactDTO> create(@RequestBody ContactDTO dto, Authentication authentication) {
        ContactDTO created = service.create(dto, authentication);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContactDTO> update(@PathVariable Long id, @RequestBody ContactDTO dto, Authentication authentication) {
        ContactDTO updated = service.update(id, dto, authentication);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
        service.delete(id, authentication);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}