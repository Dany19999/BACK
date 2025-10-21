package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table; // 👈 NECESARIO: Importar la anotación Table

@Entity
@Table(name = "app_user") // 👈 CORRECCIÓN: Renombra la tabla a un nombre seguro
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String cargo;
    private String role;     // Ejemplo: "ADMIN" o "USER"

    // Constructor vacío (necesario para JPA)
    public User() {}

    // Constructor para inicializar
    public User(String username, String password, String cargo, String role) {
        this.username = username;
        this.password = password;
        this.cargo = cargo;
        this.role = role;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // --- Getters y Setters ---

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}