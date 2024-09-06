package com.boris.backend.usersapp.backendusersapp.services;

import com.boris.backend.usersapp.backendusersapp.models.entities.Document;
import com.boris.backend.usersapp.backendusersapp.repositories.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    public Document save(Document document) {
        return documentRepository.save(document);
    }

    public List<Document> findAll() {
        return documentRepository.findAll();
    }

    public Optional<Document> findById(Long id) {
        return documentRepository.findById(id);
    }

    public void deleteById(Long id) {
        documentRepository.deleteById(id);
    }
}