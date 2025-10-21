package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.AttendanceRecord;
import com.example.demo.service.AttendanceService;

@Controller
@RequestMapping("/admin/attendance") // URL base: /admin/attendance
public class AttendanceController {
    
    // Inyección de Dependencia: Conecta la capa Web con la lógica de negocio
    @Autowired
    private AttendanceService attendanceService;

    // 1. Ver todos los registros.
    // GET /admin/attendance/view
    @GetMapping("/view")
    public String viewAllRecords(Model model) {
        
        // Llama al servicio para obtener la lista de todos los registros de asistencia
        List<AttendanceRecord> records = attendanceService.findAllRecords();
        
        // Añade la lista al modelo para que la vista 'verregistros.html' pueda iterar sobre ella
        model.addAttribute("records", records);
        
        // NOTA: Asegúrate de que el nombre del archivo en templates/admin/attendance/ sea verregistros.html
        return "admin/attendance/records";
    }

    // 2. Generar reportes globales.
    // GET /admin/attendance/reports
    @GetMapping("/reports")
    public String generateReports(Model model) {
        
        // Llama al servicio para ejecutar la lógica del reporte y obtener el resultado
        String reportMessage = attendanceService.generateGlobalReport();
        
        // Añade el mensaje del reporte al modelo
        model.addAttribute("reportMessage", reportMessage);
        
        return "admin/attendance/reports";
    }
}