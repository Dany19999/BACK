package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired; // Importa el servicio de lógica de negocio
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;

@Controller
@RequestMapping("/admin/users") // Mapea la ruta base para el CRUD
public class UserController {

    // Inyección de la Capa de Servicio (Lógica de Negocio)
    @Autowired
    private UserService userService;

    // 1. R (READ): Listar todos los usuarios
    // GET /admin/users
    @GetMapping
    public String listUsers(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "admin/users/list"; // Muestra la tabla (list.html)
    }

    // 2. C (CREATE): Mostrar formulario para nuevo usuario
    // GET /admin/users/new
    @GetMapping("/new")
    public String newUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("title", "Crear Nuevo Usuario");
        return "admin/users/form"; // Muestra el formulario (form.html)
    }

    // 3. U (UPDATE): Mostrar formulario para editar usuario
    // GET /admin/users/edit/{id}
    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        // Busca el usuario por ID y lo pone en el modelo
        User user = userService.findById(id).orElseThrow(() -> new IllegalArgumentException("ID de usuario inválido:" + id));
        model.addAttribute("user", user);
        model.addAttribute("title", "Editar Usuario: " + user.getUsername());
        return "admin/users/form"; // Reutiliza el formulario
    }

    // 4. C/U (CREATE/UPDATE): Manejar el guardado desde el formulario
    // POST /admin/users
    @PostMapping
    public String saveUser(
    // 1. Capturamos el ID como un parámetro opcional de la URL/Formulario
    @RequestParam(value = "id", required = false) Long id,
    // 2. Capturamos el resto del objeto 'User'
    @ModelAttribute User user)
{
    // 3. Si se recibió un ID, lo asignamos al objeto 'User' (ESTE ES EL PASO CRÍTICO)
    if (id != null) {
        user.setId(id);
    }
    
    // Llama al servicio. Ahora 'user' SIEMPRE tendrá el ID si existe.
    userService.save(user);
    return "redirect:/admin/users";
}

    // 5. D (DELETE): Eliminar usuario
    // GET /admin/users/delete/{id}
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/admin/users"; // Redirige de vuelta a la lista
    }
}