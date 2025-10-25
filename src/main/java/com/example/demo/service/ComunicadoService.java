package com.example.demo.service;

import java.util.Arrays; // <-- Importación para Arrays
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Comunicado;
import com.example.demo.repository.ComunicadoRepository;
import com.example.demo.repository.UserRepository;

@Service
public class ComunicadoService {

    @Autowired
    private ComunicadoRepository comunicadoRepository;
    
    @Autowired
    private UserRepository userRepository;

    // ... (Métodos findAll, save, delete) ...

    // MÉTODO CLAVE CORREGIDO: Leer comunicados dirigidos a un usuario específico
    public List<Comunicado> findForUser(Long userId) {
        List<Comunicado> allComunicados = findAll();
        
        final String userIdString = String.valueOf(userId);
        
        return allComunicados.stream()
            .filter(c -> {
                String destinatarios = c.getDestinatariosIds();
                
                // CASO 1: Comunicado para TODOS (El campo es nulo o vacío)
                if (destinatarios == null || destinatarios.isEmpty()) {
                    return true;
                }
                
                // CASO 2: Filtrado por IDs individuales (Tokenización)
                // Dividimos la cadena "1,5,9" en tokens y verificamos la igualdad exacta.
                return Arrays.stream(destinatarios.split(","))
                            .anyMatch(id -> id.trim().equals(userIdString));
            })
            .collect(java.util.stream.Collectors.toList());
    }

    // ... (El resto de tus métodos findAll, save, delete) ...
    public List<Comunicado> findAll() {
        return comunicadoRepository.findAll(Sort.by(Sort.Direction.DESC, "fechaPublicacion"));
    }

    public Comunicado save(Comunicado comunicado) {
        return comunicadoRepository.save(comunicado);
    }

    public void delete(Long id) {
        comunicadoRepository.deleteById(id);
    }

    public Optional<Comunicado> findById(Long id) {
    return comunicadoRepository.findById(id);
}
}