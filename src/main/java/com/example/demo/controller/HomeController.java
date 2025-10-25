package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.entity.AttendanceRecord;
import com.example.demo.entity.Comunicado;
import com.example.demo.entity.User; // <-- NUEVA IMPORTACIÓN
import com.example.demo.service.AttendanceService;
import com.example.demo.service.ComunicadoService;
import com.example.demo.service.UserService; // <-- NUEVA IMPORTACIÓN

@Controller
public class HomeController {

    // Dependencias
    @Autowired
    private UserService userService;

    @Autowired
    private AttendanceService attendanceService;
    
    @Autowired // <-- DEPENDENCIA AÑADIDA
    private ComunicadoService comunicadoService; 

    @GetMapping("/")
    public String home(Authentication auth) {
        if (auth != null && auth.isAuthenticated()) {
            // Si es ADMIN, redirige al panel de administrador
            if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
                return "redirect:/admin/dashboard";
            }
            // Si es USER, redirige al panel de usuario
            if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("USER"))) {
                return "redirect:/user/dashboard";
            }
        }
        return "redirect:/login";
    }
    
    // Ruta para el dashboard de usuario
    @GetMapping("/user/dashboard")
    public String userDashboard(Authentication auth, Model model) {
        
        // 1. Obtener el usuario completo para su ID
        String userName = auth.getName();
        User user = userService.findByUsername(userName)
                .orElseThrow(() -> new RuntimeException("Error: Usuario no encontrado al cargar dashboard."));
        
        Long userId = user.getId(); // El ID es necesario para los filtros

        // 2. Cargar el historial de asistencia personal
        List<AttendanceRecord> records = attendanceService.findPersonalRecords(userId);
        model.addAttribute("personalRecords", records);

        // 3. CÓDIGO AÑADIDO: Cargar los comunicados dirigidos a ESTE usuario
        List<Comunicado> comunicados = comunicadoService.findForUser(userId);
        model.addAttribute("comunicados", comunicados); // <-- PASAMOS LA LISTA FILTRADA AL MODELO

        return "user/dashboard"; // templates/user/dashboard.html
    }
}