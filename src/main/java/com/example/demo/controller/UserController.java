package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired; // Necesario para Optional
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.User;
import com.example.demo.service.UserService; // Asegúrate de que esta importación apunte a la carpeta 'entity'

@Controller
@RequestMapping("/admin/users")
public class UserController {

    @Autowired
    private UserService userService;

    // 1. R (READ): Listar todos los usuarios y gestionar la búsqueda
    // GET /admin/users
    @GetMapping
    public String listUsers(Model model, 
                            @RequestParam(value = "keyword", required = false) String keyword) { // <-- AÑADIDO PARÁMETRO
        
        List<User> users;
        
        // Lógica del Buscador
        if (keyword != null && !keyword.trim().isEmpty()) {
            // Busca por nombre de usuario (ejemplo de búsqueda)
            users = userService.searchByUsername(keyword.trim()); // <-- USA EL NUEVO MÉTODO
            model.addAttribute("keyword", keyword);
        } else {
            // Si no hay palabra clave, trae todos
            users = userService.findAll();
        }

        model.addAttribute("users", users);
        return "admin/users/list";
    }

    // 2. C (CREATE): Mostrar formulario para nuevo usuario
    @GetMapping("/new")
    public String newUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("title", "Crear Nuevo Usuario");
        return "admin/users/form";
    }

    // 3. U (UPDATE): Mostrar formulario para editar usuario
    @GetMapping("/edit/{id}")
    public String editUserForm(Model model, @PathVariable Long id) { // Intercambié el orden de los argumentos
        // Busca el usuario por ID y lo pone en el modelo
        User user = userService.findById(id).orElseThrow(() -> new IllegalArgumentException("ID de usuario inválido:" + id));
        model.addAttribute("user", user);
        model.addAttribute("title", "Editar Usuario: " + user.getUsername());
        return "admin/users/form";
    }

    // 4. C/U (CREATE/UPDATE): Manejar el guardado desde el formulario
    @PostMapping
    public String saveUser(@RequestParam(value = "id", required = false) Long id, @ModelAttribute User user) {
        
        // Asignación manual de ID para forzar el UPDATE (solución al problema de duplicación)
        if (id != null) {
            user.setId(id); // <--- Necesita el método setId(Long) en User.java
        }
        
        userService.save(user); // Llama al servicio que hashea la contraseña
        return "redirect:/admin/users";
    }

    // 5. D (DELETE): Eliminar usuario
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/admin/users";
    }
}