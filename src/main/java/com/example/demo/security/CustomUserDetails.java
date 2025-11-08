package com.example.demo.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.entity.User;

/**
 * Clase Wrapper para la entidad User.
 * Expone las propiedades de la entidad User a Spring Security y Thymeleaf.
 */
public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }
    
    // --- MÉTODOS REQUERIDOS POR SPRING SECURITY ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(user.getRole()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    private Long id; 

// Debes añadir el método getId()
    public Long getId() {
    return id; 
}

    // CLAVE: Devuelve el nombre de usuario de tu entidad, no el genérico de Spring.
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // Método opcional: Permite a Thymeleaf acceder al objeto User completo (ej: user.cargo)
    public User getUser() {
        return user;
    }
    
    // Métodos de cuenta
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}