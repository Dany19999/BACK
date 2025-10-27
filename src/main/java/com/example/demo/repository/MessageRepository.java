package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
    
    // Empleado: Ver todos sus mensajes
    List<Message> findByEmployeeIdOrderBySentDateDesc(Long employeeId);
    
    // Administrador: Ver mensajes pendientes
    List<Message> findByStatusOrderBySentDateAsc(String status);
}