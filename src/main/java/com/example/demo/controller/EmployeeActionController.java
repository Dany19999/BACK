package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.service.AttendanceService;

// Esta clase solo debe manejar las ACCIONES de POST
@Controller
@RequestMapping("/user/action")
public class EmployeeActionController {
    
    @Autowired
    private AttendanceService attendanceService;
    
    // NOTA: La lógica de showDashboard (GET) fue movida a HomeController o un nuevo controlador.

    // POST /user/action/entry
    @PostMapping("/entry")
    public String markEntry(Authentication auth, RedirectAttributes ra) {
        
        String userName = auth.getName(); 
        
        // El servicio retorna un booleano (true si fue exitoso)
        if (attendanceService.markEntry(userName)) {
            ra.addFlashAttribute("message", "✅ Entrada marcada con éxito!");
        } else {
            ra.addFlashAttribute("error", "❌ Ya tienes una sesión de asistencia abierta.");
        }
        
        return "redirect:/user/dashboard";
    }
    
    // POST /user/action/exit
    @PostMapping("/exit")
    public String markExit(Authentication auth, RedirectAttributes ra) {
        
        String userName = auth.getName(); 
        
        if (attendanceService.markExit(userName)) {
            ra.addFlashAttribute("message", "✅ Salida marcada con éxito!");
        } else {
            ra.addFlashAttribute("error", "❌ No hay entrada pendiente para marcar salida.");
        }
        
        return "redirect:/user/dashboard";
    }
}