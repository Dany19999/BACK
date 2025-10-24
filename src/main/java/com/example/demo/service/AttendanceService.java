package com.example.demo.service;

import java.time.Duration; // NECESARIO para calcular la duración
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
    private UserRepository userRepository;

    // MÉTODO PRIVADO: Calcula la diferencia y la asigna al campo @Transient
    private void calculateDuration(AttendanceRecord record) {
        // Solo calcular si la sesión está cerrada (exitTime no es null)
        if (record.getExitTime() != null) {
            
            // 1. Calcula la diferencia de tiempo
            Duration duration = Duration.between(record.getEntryTime(), record.getExitTime());
            
            // 2. Convierte la duración total a horas (con decimales)
            double hours = duration.toMinutes() / 60.0;
            
            // 3. Limita a dos decimales y guarda en el campo transitorio
            record.setDurationHours(Math.round(hours * 100.0) / 100.0);
            
        } else if (record.getUserName() != null && record.getUserName().contains("FALTA AUTOMÁTICA")) {
            // Manejar el caso de falta para que muestre 0 horas
            record.setDurationHours(0.0);
        }
    }

    // ----------------------------------------------------------------------
    // MÉTODOS DE LECTURA (APLICAN EL CÁLCULO)
    // ----------------------------------------------------------------------
    
    // Lógica para obtener todos los registros (Usado por Admin)
    public List<AttendanceRecord> findAllRecords() {
        List<AttendanceRecord> records = attendanceRepository.findAll();
        for (AttendanceRecord record : records) {
            calculateDuration(record); // <-- APLICAR CÁLCULO
        }
        return records;
    }
    
    // R - Lógica para obtener solo los registros del usuario logueado
    public List<AttendanceRecord> findPersonalRecords(Long userId) {
        List<AttendanceRecord> records = attendanceRepository.findByUserIdOrderByEntryTimeDesc(userId);
        for (AttendanceRecord record : records) {
            calculateDuration(record); // <-- APLICAR CÁLCULO
        }
        return records;
    }

    // ----------------------------------------------------------------------
    // MÉTODOS DE ESCRITURA (NO MODIFICADOS)
    // ----------------------------------------------------------------------

    // C - Lógica para marcar la ENTRADA (Incluye control de sesión abierta)
    public boolean markEntry(String userName) {
        
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado al marcar entrada."));
        
        Long userId = user.getId();
        LocalDateTime now = LocalDateTime.now();
        
        // VALIDACIÓN DE ENTRADA ÚNICA POR DÍA (y Sesión Abierta)
        LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1); 
        
        List<AttendanceRecord> todayRecords = attendanceRepository.findByUserIdAndEntryTimeBetween(userId, startOfDay, endOfDay);
        if (!todayRecords.isEmpty()) { return false; } 
        
        List<AttendanceRecord> openRecords = attendanceRepository.findByUserIdAndExitTimeIsNull(userId);
        if (!openRecords.isEmpty()) { return false; } 
        
        AttendanceRecord record = new AttendanceRecord(userId, user.getUsername(), now);
        attendanceRepository.save(record);
        return true;
    }

    // U - Lógica para marcar la SALIDA
    public boolean markExit(String userName) {
        
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado al marcar salida."));
        
        Long userId = user.getId();

        List<AttendanceRecord> records = attendanceRepository.findByUserIdAndExitTimeIsNull(userId);

        if (!records.isEmpty()) {
            AttendanceRecord openRecord = records.get(0);
            openRecord.setExitTime(LocalDateTime.now());
            attendanceRepository.save(openRecord);
            return true;
        }
        return false;
    }
    
    // Lógica para generar reportes globales (Ejemplo simple)
    public String generateGlobalReport() {
        long totalRecords = attendanceRepository.count();
        return "Reporte Generado: Se encontraron " + totalRecords + " registros de asistencia.";
    }
}