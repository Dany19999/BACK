package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // Necesario para obtener el nombre del Admin

import com.example.demo.entity.Message;
import com.example.demo.service.MessageService;
import com.example.demo.service.UserService; // Asumo que esta importación es necesaria para obtener el nombre del Admin

@Controller
@RequestMapping("/admin/messages") // Base URL: /admin/messages
public class AdminMessageController {

    @Autowired
    private MessageService messageService;
    
    // Inyección necesaria para la lógica (ej., obtener detalles del Admin)
    @Autowired 
    private UserService userService; 

    // -----------------------------------------------------
    // R: Muestra todos los mensajes pendientes (Bandeja de entrada)
    // -----------------------------------------------------
    @GetMapping
    public String viewPendingMessages(Model model) {
        // Llama al servicio para obtener solo los mensajes con estado "PENDIENTE"
        List<Message> pendingMessages = messageService.findPendingMessages();
        model.addAttribute("messages", pendingMessages);
        return "messages/admin_list"; // Vista que lista los tickets
    }

    // -----------------------------------------------------
    // U: Muestra el formulario para responder a un mensaje (Chat View)
    // -----------------------------------------------------
    @GetMapping("/respond/{id}")
    public String respondForm(@PathVariable Long id, Model model) {
        // Cargar el mensaje original que el Admin está respondiendo
        Message message = messageService.findById(id);
        
        if (message == null) {
            return "redirect:/admin/messages";
        }
        
        // Carga la información del ticket para el formulario de respuesta
        model.addAttribute("message", message); 
        
        // Devolvemos la vista del formulario de respuesta para que el Admin escriba
        return "messages/admin_respond"; 
    }

    // -----------------------------------------------------
    // U: Procesa el envío de la respuesta (POST)
    // -----------------------------------------------------
    @PostMapping("/respond/{id}")
    public String processResponse(@PathVariable Long id, 
                                  @RequestParam("adminResponse") String response, // Captura el texto de respuesta
                                  Authentication auth, // Usamos esto para obtener el nombre del Admin
                                RedirectAttributes ra) {
        
        try {
            // Llama al servicio para guardar la respuesta, cambiar el estado a RESPONDIDO
            messageService.respondToMessage(id, response, auth.getName());
            ra.addFlashAttribute("message", "Respuesta enviada al empleado con éxito.");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        
        // Redirige al Administrador a la bandeja de entrada para que siga revisando
        return "redirect:/admin/messages"; 
    }
}