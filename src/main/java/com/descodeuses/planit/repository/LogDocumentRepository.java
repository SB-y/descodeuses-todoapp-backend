package com.descodeuses.planit.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.descodeuses.planit.entity.LogDocument;

public interface LogDocumentRepository extends MongoRepository<LogDocument, String> {
    
}
