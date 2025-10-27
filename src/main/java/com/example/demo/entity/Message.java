package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String subject;     // Asunto o Título del mensaje
    
    @Lob 
    private String employeeMessage; // Mensaje original del empleado
    
    @Lob 
    private String adminResponse;   // Respuesta del administrador
    
    private Long employeeId;
    private String employeeUsername;
    
    private String status;          // ESTADO: PENDIENTE, RESPONDIDO
    private LocalDateTime sentDate;
    private LocalDateTime responseDate;

    public Message() {
        this.status = "PENDIENTE";
        this.sentDate = LocalDateTime.now();
    }
    
    // --- Getters y Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getEmployeeMessage() { return employeeMessage; }
    public void setEmployeeMessage(String employeeMessage) { this.employeeMessage = employeeMessage; }

    public String getAdminResponse() { return adminResponse; }
    // ESTE SETTER RESUELVE EL ERROR DE COMPILACIÓN:
    public void setAdminResponse(String adminResponse) {
        this.adminResponse = adminResponse;
    }

    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }

    public String getEmployeeUsername() { return employeeUsername; }
    public void setEmployeeUsername(String employeeUsername) { this.employeeUsername = employeeUsername; }

    public String getStatus() { return status; }
    // ESTE SETTER RESUELVE EL ERROR DE COMPILACIÓN:
    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getSentDate() { return sentDate; }
    public void setSentDate(LocalDateTime sentDate) { this.sentDate = sentDate; }

    public LocalDateTime getResponseDate() { return responseDate; }
    // ESTE SETTER RESUELVE EL ERROR DE COMPILACIÓN:
    public void setResponseDate(LocalDateTime responseDate) {
        this.responseDate = responseDate;
    }
}