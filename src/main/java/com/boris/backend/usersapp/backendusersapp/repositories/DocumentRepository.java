package com.boris.backend.usersapp.backendusersapp.repositories;

import com.boris.backend.usersapp.backendusersapp.models.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
}