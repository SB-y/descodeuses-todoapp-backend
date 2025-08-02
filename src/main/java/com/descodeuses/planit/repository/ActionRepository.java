package com.descodeuses.planit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.descodeuses.planit.entity.ActionEntity;
import com.descodeuses.planit.entity.UtilisateurEntity;


//                            ... extends JpaRepository<TYPE OBJET, TYPE ID OBJET>
//TYPE OBJET = Action
//TYPE ID OBJET = Type du champ id = Long

@Repository
public interface ActionRepository extends JpaRepository<ActionEntity, Long> {
// pas besoin de définir des méthodes donc ActionRepository est une interface

List<ActionEntity> findByUtilisateur(UtilisateurEntity utilisateur);
}
