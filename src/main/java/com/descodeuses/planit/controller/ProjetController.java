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


import com.descodeuses.planit.dto.ProjetDTO;
import com.descodeuses.planit.service.ProjetService;

@RestController
@RequestMapping("/api/projet")


public class ProjetController {

    private final ProjetService service;

    public ProjetController(ProjetService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjetDTO> getById(@PathVariable Long id, Authentication authentication) {
        ProjetDTO projetDTO = service.getById(id, authentication);
        return new ResponseEntity<>(projetDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ProjetDTO>> getAll(Authentication authentication) {
        List<ProjetDTO> items = service.getAllByUser(authentication);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProjetDTO> create(@RequestBody ProjetDTO dto, Authentication authentication) {
        ProjetDTO created = service.create(dto, authentication);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjetDTO> update(@PathVariable Long id, @RequestBody ProjetDTO dto, Authentication authentication) {
        ProjetDTO updated = service.update(id, dto, authentication);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
        service.delete(id, authentication);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
