package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        
        // --- Contraseña Común para Pruebas ---
        String rawPassword = "123456";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // --- 1. Crear el usuario ADMIN ---
        if (userRepository.findByUsername("admin").isEmpty()) {
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword(encodedPassword);
            adminUser.setRole("ADMIN");
            adminUser.setCargo("Administrador Principal"); // Asegúrate de asignar el campo cargo
            userRepository.save(adminUser);
            System.out.println("✅ Usuario ADMIN de prueba creado. Contraseña: " + rawPassword);
        }

        // --- 2. Crear el usuario USER para el Dashboard de Empleado (LA SOLUCIÓN AL ERROR) ---
        if (userRepository.findByUsername("empleado").isEmpty()) {
            User user = new User();
            user.setUsername("empleado");
            user.setPassword(encodedPassword);
            user.setRole("USER"); // Rol que activa la redirección a /user/dashboard
            user.setCargo("Operario Estándar");
            userRepository.save(user);
            System.out.println("✅ Usuario USER de prueba creado. Contraseña: " + rawPassword);
        }
    }
}