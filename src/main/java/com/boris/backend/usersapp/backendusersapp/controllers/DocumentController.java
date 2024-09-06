package com.boris.backend.usersapp.backendusersapp.controllers;

import com.boris.backend.usersapp.backendusersapp.models.entities.Document;
import com.boris.backend.usersapp.backendusersapp.services.DocumentService;
import com.boris.backend.usersapp.backendusersapp.services.DriveQuickstart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/documents")
@CrossOrigin(origins = "*")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private DriveQuickstart googleDriveService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("name") MultipartFile file) {
        try {
            // Convertir MultipartFile a java.io.File
            Path tempDir = Files.createTempDirectory("");
            File tempFile = tempDir.resolve(file.getOriginalFilename()).toFile();
            file.transferTo(tempFile);

            // Subir archivo a Google Drive
            String fileId = googleDriveService.uploadFile(tempFile, file.getContentType());

            // Crear y guardar el documento en la base de datos
            Document document = new Document();
            document.setFileId(fileId);
            document.setName(file.getOriginalFilename());
            Document savedDocument = documentService.save(document);

            // Eliminar archivo temporal
            tempFile.delete();

            return ResponseEntity.status(HttpStatus.CREATED).body(savedDocument);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al subir el archivo");
        }
    }

    @GetMapping
    public ResponseEntity<List<Document>> getAllDocuments() {
        List<Document> documents = documentService.findAll();
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDocumentById(@PathVariable Long id) {
        Optional<Document> document = documentService.findById(id);
        if (document.isPresent()) {
            return ResponseEntity.ok(document.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Documento no encontrado");
        }
    }
}