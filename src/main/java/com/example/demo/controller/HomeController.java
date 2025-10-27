package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.entity.AttendanceRecord;
import com.example.demo.entity.Comunicado;
import com.example.demo.entity.User;
import com.example.demo.service.AttendanceService;
import com.example.demo.service.ComunicadoService;
import com.example.demo.service.UserService;

@Controller
public class HomeController {

    // Dependencias
    @Autowired
    private UserService userService;

    @Autowired
    private AttendanceService attendanceService;
    
    @Autowired
    private ComunicadoService comunicadoService; 

    // -----------------------------------------------------
    // 1. RUTA RAÍZ: Maneja la redirección después del login
    // -----------------------------------------------------
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
    
    // -----------------------------------------------------
    // 2. DASHBOARD DEL EMPLEADO (/user/dashboard)
    // -----------------------------------------------------
    @GetMapping("/user/dashboard")
    public String userDashboard(Authentication auth, Model model) {
        
        // 1. Obtener el nombre del usuario logueado (lo usaremos para el display)
        String userName = auth.getName();
        
        // 2. Buscar el objeto User completo para obtener el ID
        User user = userService.findByUsername(userName)
                .orElseThrow(() -> new RuntimeException("Error: Usuario no encontrado al cargar dashboard."));
        
        Long userId = user.getId(); 

        // 3. Cargar datos de los módulos
        List<AttendanceRecord> records = attendanceService.findPersonalRecords(userId);
        List<Comunicado> comunicados = comunicadoService.findForUser(userId);
        
        // 4. PASAR VARIABLES AL MODELO
        
        // SOLUCIÓN AL PROBLEMA VISUAL: Pasar el nombre de usuario explícitamente al modelo
        model.addAttribute("userNameDisplay", userName); 

        // Pasar datos de Asistencia y Comunicados
        model.addAttribute("personalRecords", records);
        model.addAttribute("comunicados", comunicados);

        return "user/dashboard"; // templates/user/dashboard.html
    }
}