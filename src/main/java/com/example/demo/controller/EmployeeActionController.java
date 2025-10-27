package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; 
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Message; 
import com.example.demo.entity.User;
import com.example.demo.service.AttendanceService;
import com.example.demo.service.MessageService; 
import com.example.demo.service.UserService;
// ----------------------------------------------

@Controller
@RequestMapping("/user")
public class EmployeeActionController {
    
    @Autowired
    private AttendanceService attendanceService;
    
    @Autowired
    private UserService userService;
    
    @Autowired 
    private MessageService messageService; 

    // -----------------------------------------------------
    // MÓDULO DE MENSAJERÍA (GET /user/messages)
    // -----------------------------------------------------
    @GetMapping("/messages")
    public String viewMessages(Authentication auth, Model model) {
        
        // 1. Obtener el ID del empleado logueado
        String userName = auth.getName();
        User employee = userService.findByUsername(userName).orElseThrow(); 
        Long employeeId = employee.getId();
        
        // 2. Carga los mensajes del empleado
        List<Message> messages = messageService.findMessagesByEmployee(employeeId);
        model.addAttribute("messages", messages);
        
        // 3. Objeto vacío para el formulario de nuevo mensaje
        model.addAttribute("newMessage", new Message()); 
        
        return "messages/employee_view"; // Vista que debes crear
    }
    
    // -----------------------------------------------------
    // MÉTODO NUEVO: Enviar Mensaje (POST /user/messages)
    // -----------------------------------------------------
    @PostMapping("/messages")
    public String sendMessage(@ModelAttribute Message message, 
                              Authentication auth, 
                              RedirectAttributes ra) {
        
        // 1. Obtener el ID y nombre del empleado
        String userName = auth.getName();
        User employee = userService.findByUsername(userName).orElseThrow(); 
        
        // 2. Asignar los datos de identidad al mensaje
        message.setEmployeeId(employee.getId());
        message.setEmployeeUsername(userName);
        
        // 3. Guardar el mensaje (el servicio asigna el estado PENDIENTE)
        messageService.createMessage(message); 
        
        ra.addFlashAttribute("message", "✅ Mensaje enviado al administrador con éxito.");
        
        return "redirect:/user/messages";
    }

    // -----------------------------------------------------
    // MÓDULO DE ASISTENCIA (POST /user/action/...)
    // -----------------------------------------------------
    @PostMapping("/action/entry")
    public String markEntry(Authentication auth, RedirectAttributes ra) {
        String userName = auth.getName();
        if (attendanceService.markEntry(userName)) {
            ra.addFlashAttribute("message", "✅ Entrada marcada con éxito!");
        } else {
            ra.addFlashAttribute("error", "❌ Ya tienes una sesión de asistencia abierta.");
        }
        return "redirect:/user/dashboard";
    }

    @PostMapping("/action/exit")
    public String markExit(Authentication auth, RedirectAttributes ra) {
        String userName = auth.getName();
        if (attendanceService.markExit(userName)) {
            ra.addFlashAttribute("message", "✅ Salida marcada con éxito!");
        } else {
            ra.addFlashAttribute("error", "❌ No hay entrada pendiente para marcar salida.");
        }
        return "redirect:/user/dashboard";
    }
    
    // RUTA DASHBOARD
}