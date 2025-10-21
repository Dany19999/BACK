package com.example.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.AttendanceRecord;

// JpaRepository: <Entidad, Tipo de ID>
public interface AttendanceRepository extends JpaRepository<AttendanceRecord, Long> {

    // 1. Método para Control de Sesión Abierta (¡Ya lo tenías!)
    // Busca registros por userId donde exitTime sea NULL (sesión abierta)
    List<AttendanceRecord> findByUserIdAndExitTimeIsNull(Long userId);
    
    // 2. MÉTODO FALTANTE PARA EL HISTORIAL PERSONAL:
    // Obtener todos los registros de un usuario, ordenados por entrada (más reciente primero)
    List<AttendanceRecord> findByUserIdOrderByEntryTimeDesc(Long userId);

    // 3. CONTROL DIARIO: Busca cualquier registro (abierto o cerrado) entre dos horas (día actual)
    List<AttendanceRecord> findByUserIdAndEntryTimeBetween(Long userId, LocalDateTime startOfDay, LocalDateTime endOfDay);
}