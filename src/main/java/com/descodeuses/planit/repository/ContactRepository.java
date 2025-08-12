package com.descodeuses.planit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.descodeuses.planit.entity.ContactEntity;
import com.descodeuses.planit.entity.UtilisateurEntity;


@Repository
public interface ContactRepository extends JpaRepository<ContactEntity, Long> {

    List<ContactEntity> findByUtilisateur(UtilisateurEntity utilisateur);
}