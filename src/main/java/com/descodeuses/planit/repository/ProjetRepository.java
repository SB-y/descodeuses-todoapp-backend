package com.descodeuses.planit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.descodeuses.planit.entity.ProjetEntity;
import com.descodeuses.planit.entity.UtilisateurEntity;

@Repository
public interface ProjetRepository extends JpaRepository<ProjetEntity, Long> {
    List<ProjetEntity> findByUtilisateur(UtilisateurEntity utilisateur);
}