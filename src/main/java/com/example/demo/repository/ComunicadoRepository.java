package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Comunicado;

public interface ComunicadoRepository extends JpaRepository<Comunicado, Long> {
    // Los métodos findAll, save, deleteById están disponibles por defecto.
}