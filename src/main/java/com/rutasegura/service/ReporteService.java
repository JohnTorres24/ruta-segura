package com.rutasegura.service;

import com.rutasegura.dto.ReporteCreateDto;
import com.rutasegura.dto.ReporteResponseDto;
import com.rutasegura.model.CategoriaRiesgo;

import java.util.List;

/**
 * Contrato de servicios para el registro rápido y consulta de reportes de riesgo.
 */
public interface ReporteService {

    ReporteResponseDto crearReporte(ReporteCreateDto createDto, String emailUsuaria);

    List<ReporteResponseDto> listarReportes(CategoriaRiesgo categoria, String municipio, Double latitud, Double longitud);

    ReporteResponseDto obtenerReportePorId(Long id);

    java.util.Map<String, Long> obtenerEstadisticas();
}
