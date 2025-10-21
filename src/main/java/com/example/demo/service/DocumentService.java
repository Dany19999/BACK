package com.example.demo.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Document;
import com.example.demo.repository.DocumentRepository;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;
    
    // Directorio donde se guardarán los archivos (debe existir en la raíz)
    private static final String UPLOAD_DIR = "uploads/";

    // Guarda el archivo físicamente con un nombre único y registra los metadatos en la DB
    public Document uploadFile(MultipartFile file, Long userId, String userName) throws IOException {
        
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath); // Crea la carpeta si no existe
        }

        // 1. Obtener el nombre original del archivo para mostrar
        String originalFileName = file.getOriginalFilename();
        String extension = "";
        
        // 2. Extraer la extensión (ej: .pdf, .docx)
        if (originalFileName != null && originalFileName.contains(".")) {
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        
        // 3. Generar un nombre único basado en UUID (SOLUCIÓN A LA SOBREESCRITURA)
        String uniqueFileName = java.util.UUID.randomUUID().toString() + extension;
        
        // 4. Definir la ruta de guardado con el nombre único
        Path filePath = uploadPath.resolve(uniqueFileName); 
        
        // 5. Guardar el archivo físicamente en el servidor
        Files.copy(file.getInputStream(), filePath); 

        // 6. Registra los metadatos
        Document doc = new Document();
        doc.setFileName(originalFileName); // Guarda el nombre original para el display
        doc.setFileType(file.getContentType());
        doc.setStoragePath(filePath.toString()); // Guarda la ruta con el nombre ÚNICO
        doc.setUserId(userId);
        doc.setUploadedBy(userName);
        doc.setUploadDate(LocalDateTime.now());

        return documentRepository.save(doc);
    }
    
    // R: Obtiene todos los documentos (para el Administrador)
    public List<Document> findAllDocuments() {
        return documentRepository.findAll();
    }
    
    // R: Obtiene documentos por usuario (para el Empleado)
    public List<Document> findDocumentsByUserId(Long userId) {
        return documentRepository.findByUserIdOrderByUploadDateDesc(userId);
    }

    // R: Obtiene el path físico para descarga
    public Path getFile(Long documentId) {
        Document doc = documentRepository.findById(documentId).orElse(null);
        if (doc != null) {
            return Paths.get(doc.getStoragePath());
        }
        return null;
    }
    
    // U: Lógica para que el Administrador envíe Feedback
    public Document submitFeedback(Long documentId, String feedback, String status, String adminUsername) {
        
        Document doc = documentRepository.findById(documentId)
            .orElseThrow(() -> new RuntimeException("Documento no encontrado: " + documentId));

        doc.setStatus(status);
        doc.setAdminFeedback(feedback);
        doc.setReviewedBy(adminUsername);
        doc.setReviewDate(LocalDateTime.now());
        
        return documentRepository.save(doc);
    }
    
    // R: Buscar documento por ID (necesario para el formulario de revisión)
    public Document findById(Long documentId) {
        return documentRepository.findById(documentId).orElse(null);
    }
}