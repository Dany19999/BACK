package com.example.demo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Comunicado;
import com.example.demo.entity.User;
import com.example.demo.service.ComunicadoService;
import com.example.demo.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    // INYECCIONES REQUERIDAS
    @Autowired
    private UserService userService;
    
    @Autowired
    private ComunicadoService comunicadoService;

    // ELIMINADA: private NotificationService notificationService;

    // -------------------------------------------------------------------
    // 1. DASHBOARD PRINCIPAL (Limpio)
    // -------------------------------------------------------------------
    @GetMapping("/dashboard")
    public String adminDashboard(Model model, Authentication auth) {
        
        // ELIMINADA: Lógica para cargar el contador de notificaciones pendientes
        model.addAttribute("unreadCount", 0L); // Asignamos 0 temporalmente
        
        return "dashboard"; 
    }

    // -------------------------------------------------------------------
    // 2. GESTIÓN DE COMUNICADOS DIRIGIDOS (CRUD)
    // -------------------------------------------------------------------

    @GetMapping("/comunicados")
    public String viewComunicados(Model model) {
        List<Comunicado> comunicados = comunicadoService.findAll();
        model.addAttribute("comunicados", comunicados);
        
        // Cargar TODOS los usuarios para el selector (necesario para el formulario)
        List<User> allUsers = userService.findAll(); 
        model.addAttribute("allUsers", allUsers); 
        
        model.addAttribute("newComunicado", new Comunicado()); 
        return "admin/comunicados/manage"; 
    }

    @PostMapping("/comunicados/save")
    public String saveComunicado(@ModelAttribute("newComunicado") Comunicado comunicado, 
                                 @RequestParam(value = "destinatariosIds", required = false) List<String> destinatariosIds,
                                 Authentication auth) {
        
        comunicado.setAutor(auth.getName());
        
        // Unir los IDs seleccionados en una cadena separada por comas
        if (destinatariosIds != null && !destinatariosIds.isEmpty()) {
            String ids = destinatariosIds.stream().collect(Collectors.joining(","));
            comunicado.setDestinatariosIds(ids);
        } else {
            comunicado.setDestinatariosIds(""); 
        }

        comunicadoService.save(comunicado);
        return "redirect:/admin/comunicados";
    }

    @GetMapping("/comunicados/delete/{id}")
    public String deleteComunicado(@PathVariable("id") Long id) {
        comunicadoService.delete(id);
        return "redirect:/admin/comunicados";
    }
}