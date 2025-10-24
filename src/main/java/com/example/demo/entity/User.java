package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table; // Necesario para renombrar la tabla

@Entity
@Table(name = "app_user") 
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String role;
    private String cargo;
    private String phone; // <-- Nuevo campo

    public User() {}

    // Constructor completo actualizado para el DataLoader
    public User(String username, String password, String role, String cargo, String phone) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.cargo = cargo;
        this.phone = phone;
    }

    // --- Getters y Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; } // Necesario para el UPDATE

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; } // Setter del nuevo campo
}