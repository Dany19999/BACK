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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Message;
import com.example.demo.service.MessageService;

@Controller
@RequestMapping("/admin/messages") // Base URL: /admin/messages
public class AdminMessageController {

    @Autowired
    private MessageService messageService;

    // R: Muestra todos los mensajes pendientes
    @GetMapping
    public String viewPendingMessages(Model model) {
        // Llama al servicio para obtener solo los mensajes con estado "PENDIENTE"
        List<Message> pendingMessages = messageService.findPendingMessages();
        model.addAttribute("messages", pendingMessages);
        return "messages/admin_list"; // Vista que crearemos
    }

    // U: Muestra el formulario para responder a un mensaje
    @GetMapping("/respond/{id}")
    public String respondForm(@PathVariable Long id, Model model) {
        Message message = messageService.findById(id);
        if (message == null) {
            return "redirect:/admin/messages";
        }
        model.addAttribute("message", message);
        return "messages/admin_respond"; // Vista del formulario de respuesta
    }

    // U: Procesa el envío de la respuesta (POST)
    @PostMapping("/respond/{id}")
    public String processResponse(@PathVariable Long id, 
                                  @RequestParam("adminResponse") String response,
                                  Authentication auth,
                                  RedirectAttributes ra) {
        
        try {
            // El servicio responde al mensaje y cambia el estado a RESPONDIDO
            messageService.respondToMessage(id, response, auth.getName());
            ra.addFlashAttribute("message", "Respuesta enviada al empleado con éxito.");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        
        return "redirect:/admin/messages"; // Vuelve a la bandeja de entrada
    }
}