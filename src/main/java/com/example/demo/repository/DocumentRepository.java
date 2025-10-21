package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Document;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    // Método para que el Empleado vea solo sus documentos, ordenados por fecha
    List<Document> findByUserIdOrderByUploadDateDesc(Long userId);
}