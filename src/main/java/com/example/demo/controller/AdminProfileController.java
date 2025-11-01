package com.example.demo.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller; // Importación necesaria para seguridad
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.User;
import com.example.demo.service.ProfileService;
import com.example.demo.service.UserService;

@Controller
@RequestMapping("/admin/profile") // Nueva URL: /admin/profile
@PreAuthorize("hasAuthority('ADMIN')") // Asegura que solo el ADMIN pueda acceder
public class AdminProfileController {

    @Autowired
    private UserService userService; 

    @Autowired
    private ProfileService profileService; // Servicio de perfil existente

    // -----------------------------------------------------
    // 1. GET /admin/profile: Muestra el perfil y la foto actual
    // -----------------------------------------------------
    @GetMapping
    public String viewProfile(Model model, Authentication auth) {
        
        // Carga el usuario completo logueado
        User user = userService.findByUsername(auth.getName()).orElseThrow();
        model.addAttribute("user", user);
        
        // La vista será la misma que la del empleado, pero la llamaremos 'admin_profile' 
        // para asegurar que siempre haya una distinción si la necesitamos.
        return "admin/admin_profile"; // Vista que crearemos
    }

    // -----------------------------------------------------
    // 2. POST /admin/profile/upload: Sube la foto nueva
    // -----------------------------------------------------
    @PostMapping("/upload")
    public String handleProfilePictureUpload(@RequestParam("file") MultipartFile file, 
                                            Authentication auth, 
                                            RedirectAttributes ra) {
        if (file.isEmpty()) {
            ra.addFlashAttribute("error", "Por favor selecciona un archivo.");
            return "redirect:/admin/profile";
        }
        
        try {
            User user = userService.findByUsername(auth.getName()).orElseThrow();
            profileService.saveProfilePicture(user, file);
            ra.addFlashAttribute("message", "Foto de perfil actualizada con éxito!");
        } catch (IOException e) {
            ra.addFlashAttribute("error", "Error al guardar la imagen: " + e.getMessage());
        }
        
        return "redirect:/admin/profile";
    }

    // -----------------------------------------------------
    // 3. GET /admin/profile/delete-photo: Elimina la foto actual
    // -----------------------------------------------------
    @GetMapping("/delete-photo")
    public String deleteProfilePicture(Authentication auth, RedirectAttributes ra) {
        try {
            User user = userService.findByUsername(auth.getName()).orElseThrow();
            profileService.deleteProfilePicture(user);
            ra.addFlashAttribute("message", "Foto de perfil eliminada con éxito.");
        } catch (IOException e) {
            ra.addFlashAttribute("error", "Error al eliminar la imagen: " + e.getMessage());
        }
        return "redirect:/admin/profile";
    }
}