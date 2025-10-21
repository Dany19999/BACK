package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;

@Controller
public class RegisterController {

    @Autowired
    private UserService userService;

    // GET /register: Muestra el formulario de registro
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("title", "Registro de Empleados");
        return "register"; // Busca src/main/resources/templates/register.html
    }

    // POST /register: Procesa el envío del formulario
    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        
        // 1. Asignar el rol por defecto (USER) y un cargo inicial
        user.setRole("USER");
        user.setCargo("Empleado Nuevo");
        
        // 2. Guardar el usuario (el servicio se encarga de hashear la contraseña)
        userService.save(user);
        
        // 3. Mostrar mensaje de éxito y redirigir al login
        model.addAttribute("message", "Registro exitoso. ¡Inicia sesión!");
        return "login"; // Redirige a login para que el usuario inicie sesión
    }
}