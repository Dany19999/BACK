package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class AttendanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Campo crucial: ID del usuario al que pertenece este registro
    private Long userId;
    
    // Campo para registrar la hora de entrada/salida
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    
    // (Opcional) Si quieres rastrear el cargo/nombre actual
    private String userName;

    // Constructores, Getters, y Setters

    public AttendanceRecord() {}
    
    // Constructor
    public AttendanceRecord(Long userId, String userName, LocalDateTime entryTime) {
        this.userId = userId;
        this.userName = userName;
        this.entryTime = entryTime;
    }

    // Getters y Setters (Asegúrate de incluir todos los campos)

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public LocalDateTime getEntryTime() { return entryTime; }
    public void setEntryTime(LocalDateTime entryTime) { this.entryTime = entryTime; }
    
    public LocalDateTime getExitTime() { return exitTime; }
    public void setExitTime(LocalDateTime exitTime) { this.exitTime = exitTime; }
    
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
}