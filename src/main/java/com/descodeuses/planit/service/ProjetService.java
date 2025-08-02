package com.descodeuses.planit.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;


import com.descodeuses.planit.dto.ProjetDTO;
import com.descodeuses.planit.entity.ProjetEntity;
import com.descodeuses.planit.repository.ProjetRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProjetService {



     private final ProjetRepository repository;

    public ProjetService(ProjetRepository repository) {
        this.repository = repository;
    }

    private ProjetDTO convertToDTO(ProjetEntity projet) {
        return new ProjetDTO(
                projet.getId(),
                projet.getTitle(),
                projet.getDescription());

    }

    private ProjetEntity convertToEntity(ProjetDTO projetDTO) {

        ProjetEntity projet = new ProjetEntity();
        projet.setId(projetDTO.getId());
        projet.setTitle(projetDTO.getTitle());
        projet.setDescription(projetDTO.getDescription());

        return projet;

    }

    public List<ProjetDTO> getAll() {
        List<ProjetEntity> entities = repository.findAll();

        List<ProjetDTO> projets = new ArrayList<>();

        for (ProjetEntity item : entities) {
            projets.add(convertToDTO(item));
        }

        return projets;
    }

    public ProjetDTO getById(Long id) {
        Optional<ProjetEntity> projet = repository.findById(id);

        if (projet.isEmpty()) {
            throw new EntityNotFoundException("Projet non trouvé avec id: " + id);
        }

        return convertToDTO(projet.get());

    }

    public ProjetDTO create(ProjetDTO projetDTO) {
        ProjetEntity projet = convertToEntity(projetDTO);
        ProjetEntity savedProjet = repository.save(projet);
        return convertToDTO(savedProjet);
    }

    public ProjetDTO update(Long id, ProjetDTO projetDTO) {
        ProjetEntity existingProjet = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Projet non trouvé avec id: " + id));

        existingProjet.setTitle(projetDTO.getTitle());
        existingProjet.setDescription(projetDTO.getDescription());

        ProjetEntity updatedProjet = repository.save(existingProjet);
        return convertToDTO(updatedProjet);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Projet non trouvé avec id: " + id);
        }
        repository.deleteById(id);
    }


}
