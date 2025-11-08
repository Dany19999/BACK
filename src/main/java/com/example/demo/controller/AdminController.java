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

    // -------------------------------------------------------------------
    // 1. DASHBOARD PRINCIPAL
    // -------------------------------------------------------------------
    @GetMapping("/dashboard")
    public String adminDashboard(Model model, Authentication auth) {
        
        // Lógica de contador temporal (sin NotificationService)
        model.addAttribute("unreadCount", 0L); 
        
        return "dashboard";
    }

    // -------------------------------------------------------------------
    // 2. GESTIÓN DE COMUNICADOS DIRIGIDOS (CRUD)
    // -------------------------------------------------------------------

    // R: Mostrar la vista de gestión y la tabla
    @GetMapping("/comunicados")
    public String viewComunicados(Model model) {
        List<Comunicado> comunicados = comunicadoService.findAll();
        model.addAttribute("comunicados", comunicados);
        
        // Cargar TODOS los usuarios para el selector
        List<User> allUsers = userService.findAll(); 
        model.addAttribute("allUsers", allUsers); 
        
        // Objeto para el formulario de CREACIÓN (vacío)
        model.addAttribute("newComunicado", new Comunicado()); 
        return "admin/comunicados/manage"; 
    }

    // C/U: Procesar Guardado (Crear o Actualizar)
    @PostMapping("/comunicados/save")
    public String saveComunicado(
        // Recibimos el ID oculto del formulario
        @RequestParam(value = "id", required = false) Long id, 
        @ModelAttribute("newComunicado") Comunicado comunicado, 
        @RequestParam(value = "destinatariosIds", required = false) List<String> destinatariosIds,
        Authentication auth) {
        
        // 1. Asignamos el ID (UPDATE) si existe. Esto es CLAVE.
        if (id != null) {
            comunicado.setId(id);
        }
        
        // 2. Asignamos autor y destinatarios
        comunicado.setAutor(auth.getName());
        
        if (destinatariosIds != null && !destinatariosIds.isEmpty()) {
            String ids = destinatariosIds.stream().collect(Collectors.joining(","));
            comunicado.setDestinatariosIds(ids);
        } else {
            comunicado.setDestinatariosIds(""); 
        }

        comunicadoService.save(comunicado);
        return "redirect:/admin/comunicados";
    }

    // NUEVO MÉTODO: GET para cargar el formulario con datos existentes (EDICIÓN)
    @GetMapping("/comunicados/edit/{id}")
    public String editComunicadoForm(@PathVariable("id") Long id, Model model) {
        // 1. Buscar el comunicado existente por ID (asumo que ComunicadoService tiene findById)
        Comunicado comunicado = comunicadoService.findById(id)
            .orElseThrow(() -> new RuntimeException("Comunicado no encontrado para edición: " + id));
        
        // 2. Cargar todos los usuarios para el selector
        List<User> allUsers = userService.findAll(); 
        
        // 3. Pasamos el comunicado existente al objeto "newComunicado"
        model.addAttribute("newComunicado", comunicado); 
        model.addAttribute("allUsers", allUsers);
        model.addAttribute("comunicados", comunicadoService.findAll()); // Recargar lista inferior
        
        return "admin/comunicados/manage";
    }
    

    // D: Eliminar un comunicado
    @GetMapping("/comunicados/delete/{id}")
    public String deleteComunicado(@PathVariable("id") Long id) {
        comunicadoService.delete(id);
        return "redirect:/admin/comunicados";
    }
}