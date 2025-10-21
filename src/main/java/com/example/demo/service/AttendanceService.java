package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.AttendanceRecord;
import com.example.demo.entity.User;
import com.example.demo.repository.AttendanceRepository;
import com.example.demo.repository.UserRepository;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;
    
    @Autowired
    private UserRepository userRepository; // Necesario para buscar el ID/Username

    // Lógica para obtener todos los registros (Usado por Admin)
    public List<AttendanceRecord> findAllRecords() {
        return attendanceRepository.findAll();
    }
    
    // R - Lógica para obtener solo los registros del usuario logueado
    public List<AttendanceRecord> findPersonalRecords(Long userId) {
        // Usa el método que ordenamos por fecha de entrada (más reciente primero)
        return attendanceRepository.findByUserIdOrderByEntryTimeDesc(userId);
    }


    // C - Lógica para marcar la ENTRADA (Incluye control de sesión abierta)
    public boolean markEntry(String userName) { // Retorna boolean para indicar éxito/fallo
        
        // 1. Obtener el ID del usuario
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado al marcar entrada."));
        
        Long userId = user.getId();
        LocalDateTime now = LocalDateTime.now();
        
        // --- 2. VALIDACIÓN DE ENTRADA ÚNICA POR DÍA ---
        LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1); 
        
        // Busca cualquier registro (abierto o cerrado) para hoy
        List<AttendanceRecord> todayRecords = attendanceRepository.findByUserIdAndEntryTimeBetween(userId, startOfDay, endOfDay);
        
        if (!todayRecords.isEmpty()) {
            // Fallo: ¡Ya marcó ENTRADA hoy!
            return false; 
        }
        
        // --- 3. Control de Sesión Abierta ---
        List<AttendanceRecord> openRecords = attendanceRepository.findByUserIdAndExitTimeIsNull(userId);

        if (!openRecords.isEmpty()) {
            // Fallo: Ya tiene una sesión abierta
            return false; 
        }
        
        // 4. Crear el nuevo registro de entrada
        AttendanceRecord record = new AttendanceRecord(userId, user.getUsername(), now);
        attendanceRepository.save(record);
        return true; // Éxito en la marcación
    }

    // U - Lógica para marcar la SALIDA
    public boolean markExit(String userName) {
        
        // 1. Obtener el ID del usuario
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado al marcar salida."));
        
        Long userId = user.getId();

        // 2. Buscar registros pendientes (Entradas sin Salida)
        List<AttendanceRecord> records = attendanceRepository.findByUserIdAndExitTimeIsNull(userId);

        if (!records.isEmpty()) {
            // 3. Tomar el registro más antiguo/abierto (asumimos que es el primero)
            AttendanceRecord openRecord = records.get(0);
            openRecord.setExitTime(LocalDateTime.now());
            attendanceRepository.save(openRecord);
            return true; // Éxito
        }
        return false; // No hay entrada pendiente
    }
    
    // Lógica para generar reportes globales (Ejemplo simple)
    public String generateGlobalReport() {
        long totalRecords = attendanceRepository.count();
        return "Reporte Generado: Se encontraron " + totalRecords + " registros de asistencia.";
    }
}