package com.rutasegura.dto;

import com.rutasegura.model.CategoriaRiesgo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Objeto de transferencia de datos para la visualización de un reporte en el frontend.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReporteResponseDto {

    private Long id;
    private CategoriaRiesgo categoria;
    private String municipio;
    private String referencia;
    private Double latitud;
    private Double longitud;
    private LocalDateTime fechaReporte;
    private String nombreUsuaria;
    private Double distanciaKm;
}
