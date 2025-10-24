package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.User; // Importa la entidad desde la nueva ubicación 'entity'

public interface UserRepository extends JpaRepository<User, Long> {

    // Método para Login
    Optional<User> findByUsername(String username);

    // Método para el Buscador (busca coincidencias en el username)
    List<User> findByUsernameContainingIgnoreCase(String keyword);
    
    // Método para el AttendanceScheduler
    List<User> findByRole(String role);
}