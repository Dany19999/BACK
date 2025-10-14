package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin") // 👈 Mapea todas las URLs que empiezan con /admin
public class AdminController {

    // Este método se activa cuando alguien accede a /admin/dashboard
    @GetMapping("/dashboard")
    public String adminDashboard() {
        // Spring buscará la plantilla 'dashboard.html' dentro de la carpeta 'admin'
        return "admin/dashboard"; 
    }
}
