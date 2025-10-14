package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component // Le dice a Spring que gestione esta clase y la ejecute al inicio
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository; // Necesitamos el Repositorio para guardar
    
    @Autowired
    private PasswordEncoder passwordEncoder; // Necesitamos el Codificador para hashear la contraseña

    @Override
    public void run(String... args) throws Exception {
        
        // --- 1. Definir una contraseña simple (ej: '123456') ---
        String rawPassword = "123456";
        
        // --- 2. Hashear la contraseña con BCrypt ---
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // --- 3. Crear el usuario Administrador ---
        if (userRepository.findByUsername("admin").isEmpty()) {
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword(encodedPassword); // Guarda la contraseña hasheada
            adminUser.setRole("ADMIN"); 
            
            // --- 4. Guardar en la base de datos H2 ---
            userRepository.save(adminUser);
            System.out.println("✅ Usuario ADMIN de prueba creado. Contraseña: " + rawPassword);
        }
    }
}