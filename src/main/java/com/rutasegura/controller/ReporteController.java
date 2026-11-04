package com.rutasegura.controller;

import com.rutasegura.dto.ReporteCreateDto;
import com.rutasegura.dto.ReporteResponseDto;
import com.rutasegura.model.CategoriaRiesgo;
import com.rutasegura.service.ReporteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador REST para el registro rápido y la consulta de reportes de riesgo.
 */
@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    /**
     * Endpoint protegido para registrar un reporte rápido de riesgo (One-Tap).
     * Requiere token JWT válido en el encabezado Authorization.
     */
    @PostMapping
    public ResponseEntity<ReporteResponseDto> crearReporte(
            @Valid @RequestBody ReporteCreateDto createDto,
            Authentication authentication) {
        String emailUsuaria = (authentication != null) ? authentication.getName() : null;
        if (emailUsuaria == null) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "No se encontró la identidad de la usuaria autenticada.");
        }
        ReporteResponseDto respuesta = reporteService.crearReporte(createDto, emailUsuaria);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    /**
     * Endpoint público para listar y filtrar reportes de riesgo.
     * Soporta filtrado por categoría, municipio y ordenamiento por proximidad geográfica.
     */
    @GetMapping
    public ResponseEntity<List<ReporteResponseDto>> listarReportes(
            @RequestParam(required = false) CategoriaRiesgo categoria,
            @RequestParam(required = false) String municipio,
            @RequestParam(required = false) Double latitud,
            @RequestParam(required = false) Double longitud) {
        List<ReporteResponseDto> lista = reporteService.listarReportes(categoria, municipio, latitud, longitud);
        return ResponseEntity.ok(lista);
    }

    /**
     * Endpoint público para consultar el resumen estadístico de reportes por categoría.
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<java.util.Map<String, Long>> obtenerEstadisticas() {
        return ResponseEntity.ok(reporteService.obtenerEstadisticas());
    }

    /**
     * Endpoint para consultar un reporte específico por su identificador.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReporteResponseDto> obtenerPorId(@PathVariable Long id) {
        ReporteResponseDto reporte = reporteService.obtenerReportePorId(id);
        return ResponseEntity.ok(reporte);
    }
}
