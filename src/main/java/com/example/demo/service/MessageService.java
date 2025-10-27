package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired; // Necesario para la lógica
import org.springframework.stereotype.Service;

import com.example.demo.entity.Message;
import com.example.demo.repository.MessageRepository;
import com.example.demo.repository.UserRepository;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;
    
    @Autowired
    private UserRepository userRepository; // Necesario para la lógica de Admin

    // Empleado: Crea un nuevo ticket/mensaje
    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }

    // Administrador: Obtiene todos los mensajes pendientes
    public List<Message> findPendingMessages() {
        return messageRepository.findByStatusOrderBySentDateAsc("PENDIENTE");
    }

    // Empleado: Obtiene sus mensajes
    public List<Message> findMessagesByEmployee(Long employeeId) {
        return messageRepository.findByEmployeeIdOrderBySentDateDesc(employeeId);
    }
    
    // Administrador: Responde un mensaje/ticket
    public Message respondToMessage(Long messageId, String response, String adminUsername) {
        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> new RuntimeException("Mensaje no encontrado."));
        
        message.setAdminResponse(response + " (Respondido por: " + adminUsername + ")");
        message.setResponseDate(LocalDateTime.now());
        message.setStatus("RESPONDIDO");
        
        return messageRepository.save(message);
    }
    
    // Obtiene un mensaje por ID
    public Message findById(Long id) {
        return messageRepository.findById(id).orElse(null);
    }
}