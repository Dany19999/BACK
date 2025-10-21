package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.entity.AttendanceRecord; // <-- Corregido: Ahora busca en 'entity'
import com.example.demo.entity.User; // <-- Corregido: Ahora busca en 'entity'
import com.example.demo.service.AttendanceService;
import com.example.demo.service.UserService;

@Controller
public class HomeController {

    // Dependencias Faltantes
    @Autowired
    private UserService userService;

    @Autowired
    private AttendanceService attendanceService;

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
        
        // 1. Obtener el nombre del usuario logueado
        String userName = auth.getName();
        
        // 2. Buscar el objeto User completo para obtener el ID
        User user = userService.findByUsername(userName)
                .orElseThrow(() -> new RuntimeException("Error: Usuario no encontrado al cargar dashboard."));

        // 3. Cargar el historial de asistencia personal
        List<AttendanceRecord> records = attendanceService.findPersonalRecords(user.getId());
        
        // 4. Pasar los datos a la vista para que la tabla se muestre
        model.addAttribute("personalRecords", records);

        return "user/dashboard"; // templates/user/dashboard.html
    }
}