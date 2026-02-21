package com.descodeuses.planit.repository;

import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.descodeuses.planit.entity.LogDocument;


@Profile("!test")
public interface LogDocumentRepository extends MongoRepository<LogDocument, String> {
    
}
