package com.example.demo.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

@Service
public class ProfileService {

    @Autowired
    private UserRepository userRepository;

    // Directorio donde se guardarán las fotos de perfil (CRÍTICO)
    // Este se mapeará al directorio 'uploads/profile_pictures/' en la raíz de tu proyecto.
    private static final String UPLOAD_BASE_DIR = "uploads/profile_pictures/";

    // -----------------------------------------------------
    // LÓGICA DE SUBIDA (CREATE / UPDATE)
    // -----------------------------------------------------
    public String saveProfilePicture(User user, MultipartFile file) throws IOException {
        
        // 1. Obtener la extensión del archivo
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        
        // 2. Generar un nombre único para la imagen
        String uniqueFileName = user.getId() + "_" + UUID.randomUUID().toString() + extension;
        
        // 3. Crear el directorio específico del usuario (ej: uploads/profile_pictures/1/)
        Path userDirPath = Paths.get(UPLOAD_BASE_DIR, user.getId().toString());
        if (!Files.exists(userDirPath)) {
            Files.createDirectories(userDirPath);
        }
        
        // 4. Definir la ruta completa del archivo
        Path filePath = userDirPath.resolve(uniqueFileName);
        
        // 5. Guardar el archivo físicamente
        Files.copy(file.getInputStream(), filePath);

        // 6. Si ya tenía una foto, ELIMINAR la antigua del disco (para ahorrar espacio)
        if (user.getProfilePicture() != null && !user.getProfilePicture().isEmpty()) {
            Path oldFilePath = userDirPath.resolve(user.getProfilePicture());
            Files.deleteIfExists(oldFilePath);
        }
        
        // 7. Actualizar el campo en la entidad User y guardar en la base de datos
        user.setProfilePicture(uniqueFileName);
        userRepository.save(user); // Usa el UserRepository que ya tienes
        
        return uniqueFileName;
    }

    // -----------------------------------------------------
    // LÓGICA DE ELIMINACIÓN (DELETE)
    // -----------------------------------------------------
    public void deleteProfilePicture(User user) throws IOException {
        if (user.getProfilePicture() == null || user.getProfilePicture().isEmpty()) {
            return; // No hay nada que borrar
        }
        
        // 1. Encontrar la ruta del archivo antiguo
        Path userDirPath = Paths.get(UPLOAD_BASE_DIR, user.getId().toString());
        Path filePath = userDirPath.resolve(user.getProfilePicture());
        
        // 2. Eliminar el archivo del disco
        Files.deleteIfExists(filePath);
        
        // 3. Limpiar el campo en la base de datos y guardar
        user.setProfilePicture(null);
        userRepository.save(user);
    }
}