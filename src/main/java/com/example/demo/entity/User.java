package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient; // Importación para el campo calculado de URL

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
    private String phone; 
    
    // CAMPO NUEVO: Almacenará el nombre del archivo de la foto de perfil (ej: "15_profile.jpg")
    private String profilePicture; 

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
    public void setId(Long id) { this.id = id; } 

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; } 

    // NUEVO GETTER/SETTER para el archivo de la foto
    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
    
    // MÉTODO TRANSIENTE: Para construir la URL completa de la imagen en la vista
    @Transient 
    public String getProfilePictureUrl() {
        if (profilePicture == null || profilePicture.isEmpty()) {
            // Devuelve una imagen por defecto si el usuario no tiene foto
            return "/img/default-profile.png"; 
        }
        // Devuelve la ruta completa donde la imagen será servida por el controlador
        return "/uploads/profile_pictures/" + id + "/" + profilePicture; 
    }
}