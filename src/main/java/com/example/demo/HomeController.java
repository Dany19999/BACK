package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/") // Maneja la URL raíz
    public String home() {
        // Redirige directamente al login o a la página de bienvenida
        return "redirect:/login"; 
    }
}