package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    // Este método se activa cuando alguien accede a /admin/dashboard
    @GetMapping("/dashboard")
    public String adminDashboard() {
        // CORRECCIÓN: Devuelve solo "dashboard" porque el archivo está en templates/
        // (Esto asume que el archivo dashboard.html está en src/main/resources/templates/)
        return "dashboard";
    }
}