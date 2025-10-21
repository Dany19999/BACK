package com.example.demo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Document;
import com.example.demo.entity.User;
import com.example.demo.service.DocumentService;
import com.example.demo.service.UserService;

@Controller
@RequestMapping("/documents")
public class DocumentController {

    private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);

    @Autowired
    private DocumentService documentService;

    @Autowired
    private UserService userService;

    // -----------------------------------------------------
    // A. EMPLEADO: Vista de Subida y Lista Personal
    // -----------------------------------------------------
    @GetMapping("/upload")
    public String uploadForm(Model model, Authentication auth) {
        
        User user = userService.findByUsername(auth.getName()).orElseThrow();
        List<Document> userDocs = documentService.findDocumentsByUserId(user.getId());
        
        model.addAttribute("documents", userDocs);
        return "documents/upload"; 
    }

    // --- Procesar Subida (POST) ---
    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, 
                                   Authentication auth,
                                   RedirectAttributes redirectAttributes) {
        
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "No se seleccionó ningún archivo para subir.");
            return "redirect:/documents/upload";
        }

        try {
            User user = userService.findByUsername(auth.getName()).orElseThrow();
            documentService.uploadFile(file, user.getId(), user.getUsername());
            redirectAttributes.addFlashAttribute("message", "¡Documento subido y enviado para revisión con éxito!");
            
        } catch (IOException e) {
            logger.error("Error I/O al subir archivo para el usuario {}: {}", auth.getName(), e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error interno al guardar el archivo: " + e.getMessage());
        } catch (RuntimeException e) {
            logger.error("Error de runtime al obtener usuario: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error de usuario. Intente iniciar sesión nuevamente.");
        }
        return "redirect:/documents/upload";
    }

    // -----------------------------------------------------
    // B. ADMINISTRADOR: Vista Global
    // -----------------------------------------------------
    @GetMapping("/admin/all")
    public String adminViewAll(Model model) {
        
        List<Document> allDocs = documentService.findAllDocuments();
        model.addAttribute("documents", allDocs);
        return "documents/admin_list"; 
    }

    // -----------------------------------------------------
    // C. ADMINISTRADOR: Vista de Revisión (Feedback)
    // -----------------------------------------------------
    @GetMapping("/admin/review/{docId}")
    public String reviewForm(@PathVariable Long docId, Model model) {
        Document document = documentService.findById(docId);
        
        if (document == null) {
            return "redirect:/documents/admin/all";
        }
        
        model.addAttribute("document", document);
        model.addAttribute("statuses", List.of("PENDIENTE", "REVISADO", "RECHAZADO"));
        return "documents/review_form"; 
    }

    // --- Procesar el Feedback del Administrador (POST) ---
    @PostMapping("/admin/review/{docId}")
    public String submitReview(@PathVariable Long docId,
                               @RequestParam("feedback") String feedback,
                               @RequestParam("status") String status,
                               Authentication auth,
                               RedirectAttributes redirectAttributes) {
        
        String adminUsername = auth.getName();
        documentService.submitFeedback(docId, feedback, status, adminUsername);
        
        redirectAttributes.addFlashAttribute("message", "Respuesta enviada con éxito.");
        return "redirect:/documents/admin/all"; 
    }

    // -----------------------------------------------------
    // D. GENERAL: Descargar Archivo (Requiere el directorio 'uploads/' y el archivo físico)
    // -----------------------------------------------------
    @GetMapping("/download/{docId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long docId) throws IOException {
        
        Path filePath = documentService.getFile(docId);
        if (filePath == null || !Files.exists(filePath)) {
            logger.warn("Archivo no encontrado en la ruta: {}", filePath);
            return ResponseEntity.notFound().build();
        }

        Resource resource = new UrlResource(filePath.toUri());
        String contentType = Files.probeContentType(filePath);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filePath.getFileName().toString() + "\"")
                .body(resource);
    }
}
