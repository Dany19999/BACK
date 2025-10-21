package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // C - Crear / U - Actualizar (Lógica final para no perder el hash)
    public User save(User user) {
        
        // CASO 1A: CREACIÓN DE NUEVO USUARIO
        // CASO 1B: EDICIÓN CON CAMBIO DE CONTRASEÑA
        if (user.getId() == null || (user.getPassword() != null && !user.getPassword().isEmpty())) {
            
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            
        } else {
            // CASO 2: EDICIÓN SIN CAMBIO DE CONTRASEÑA (Conservar HASH antiguo)
            
            User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado para actualización."));
            
            user.setPassword(existingUser.getPassword());
        }
        
        return userRepository.save(user);
    }

    // R - Leer todos
    public List<User> findAll() {
        return userRepository.findAll();
    }

    // R - Leer por ID (Necesario para el CRUD de edición en UserController)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    // R - Leer por Nombre de Usuario (NECESARIO para HomeController y lógica de roles)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username); 
    }

    // D - Eliminar
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}