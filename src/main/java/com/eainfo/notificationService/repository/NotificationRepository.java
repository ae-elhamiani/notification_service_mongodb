package com.eainfo.notificationService.repository;


import com.eainfo.notificationService.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, String> {
}

