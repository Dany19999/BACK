package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Define la ruta donde se guardan las imágenes (la carpeta uploads/profile_pictures/)
    private static final String UPLOAD_DIR = "uploads/profile_pictures/";

    // Este método mapea una URL pública a una ubicación física en el disco
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        
        // Mapear la URL: Cualquier solicitud que comience con /uploads/profile_pictures/
        registry.addResourceHandler("/uploads/profile_pictures/**")
                
                // Debe buscar el archivo en la ubicación física: uploads/profile_pictures/
                // Utilizamos "file:" para indicar que es un recurso del sistema de archivos local
                .addResourceLocations("file:" + UPLOAD_DIR);
    }
}