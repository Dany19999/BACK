package com.example.demo.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
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
@RequestMapping("/user/profile")
public class UserProfileController {

    @Autowired
    private UserService userService; 

    @Autowired
    private ProfileService profileService;

    // GET /user/profile: Muestra el perfil y la foto actual
    @GetMapping
    public String viewProfile(Model model, Authentication auth) {
        
        User user = userService.findByUsername(auth.getName()).orElseThrow();
        model.addAttribute("user", user);
        
        return "user/profile"; // Vista que crearemos
    }

    // POST /user/profile/upload: Sube la foto nueva
    @PostMapping("/upload")
    public String handleProfilePictureUpload(@RequestParam("file") MultipartFile file, 
                                            Authentication auth, 
                                            RedirectAttributes ra) {
        if (file.isEmpty()) {
            ra.addFlashAttribute("error", "Por favor selecciona un archivo.");
            return "redirect:/user/profile";
        }
        
        try {
            User user = userService.findByUsername(auth.getName()).orElseThrow();
            profileService.saveProfilePicture(user, file);
            ra.addFlashAttribute("message", "Foto de perfil actualizada con éxito!");
        } catch (IOException e) {
            ra.addFlashAttribute("error", "Error al guardar la imagen: " + e.getMessage());
        }
        
        return "redirect:/user/profile";
    }

    // GET /user/profile/delete: Elimina la foto actual
    @GetMapping("/delete-photo")
    public String deleteProfilePicture(Authentication auth, RedirectAttributes ra) {
        try {
            User user = userService.findByUsername(auth.getName()).orElseThrow();
            profileService.deleteProfilePicture(user);
            ra.addFlashAttribute("message", "Foto de perfil eliminada con éxito.");
        } catch (IOException e) {
            ra.addFlashAttribute("error", "Error al eliminar la imagen: " + e.getMessage());
        }
        return "redirect:/user/profile";
    }
}