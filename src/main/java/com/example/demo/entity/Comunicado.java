package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class Comunicado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Lob 
    @Column(nullable = false)
    private String contenido;

    @Column(nullable = false)
    private LocalDateTime fechaPublicacion = LocalDateTime.now();

    private String autor; 
    
    // CAMPO CLAVE: Almacena los IDs de los usuarios seleccionados (separados por comas)
    private String destinatariosIds; 
    
    // Constructores, Getters y Setters
    
    public Comunicado() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    
    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }
    
    public LocalDateTime getFechaPublicacion() { return fechaPublicacion; }
    public void setFechaPublicacion(LocalDateTime fechaPublicacion) { this.fechaPublicacion = fechaPublicacion; }
    
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public String getDestinatariosIds() { return destinatariosIds; }
    public void setDestinatariosIds(String destinatariosIds) { this.destinatariosIds = destinatariosIds; }
}