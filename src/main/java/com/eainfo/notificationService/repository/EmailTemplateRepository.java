package com.eainfo.notificationService.repository;

import com.eainfo.notificationService.model.EmailTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface EmailTemplateRepository extends MongoRepository<EmailTemplate, String> {
    Optional<EmailTemplate> findByName(String name);
}

