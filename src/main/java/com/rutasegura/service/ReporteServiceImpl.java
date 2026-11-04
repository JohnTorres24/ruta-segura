package com.rutasegura.service;

import com.rutasegura.dto.ReporteCreateDto;
import com.rutasegura.dto.ReporteResponseDto;
import com.rutasegura.exception.ResourceNotFoundException;
import com.rutasegura.model.CategoriaRiesgo;
import com.rutasegura.model.Reporte;
import com.rutasegura.model.Usuario;
import com.rutasegura.repository.ReporteRepository;
import com.rutasegura.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

/**
 * Implementación de los servicios para la gestión de reportes rápidos de riesgo.
 */
@Service
@RequiredArgsConstructor
public class ReporteServiceImpl implements ReporteService {

    private static final double RADIO_TIERRA_KM = 6371.0;

    private final ReporteRepository reporteRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public ReporteResponseDto crearReporte(ReporteCreateDto createDto, String emailUsuaria) {
        Usuario usuaria = usuarioRepository.findByEmail(emailUsuaria)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró a la usuaria asociada al correo: " + emailUsuaria));

        Reporte nuevoReporte = Reporte.builder()
                .categoria(createDto.getCategoria())
                .municipio(createDto.getMunicipio().trim())
                .referencia(createDto.getReferencia().trim())
                .latitud(createDto.getLatitud())
                .longitud(createDto.getLongitud())
                .usuario(usuaria)
                .build();

        Reporte guardado = reporteRepository.save(nuevoReporte);
        return mapearADto(guardado, null, null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReporteResponseDto> listarReportes(
            CategoriaRiesgo categoria, String municipio, Double latitud, Double longitud) {

        List<Reporte> reportes;

        if (categoria != null && municipio != null && !municipio.isBlank()) {
            reportes = reporteRepository.findByCategoriaAndMunicipioIgnoreCaseOrderByFechaReporteDesc(
                    categoria, municipio.trim());
        } else if (categoria != null) {
            reportes = reporteRepository.findByCategoriaOrderByFechaReporteDesc(categoria);
        } else if (municipio != null && !municipio.isBlank()) {
            reportes = reporteRepository.findByMunicipioIgnoreCaseOrderByFechaReporteDesc(municipio.trim());
        } else {
            reportes = reporteRepository.findAllByOrderByFechaReporteDesc();
        }

        List<ReporteResponseDto> listaDtos = reportes.stream()
                .map(r -> mapearADto(r, latitud, longitud))
                .toList();

        // Si se suministran coordenadas del usuario, se ordena por cercanía
        if (latitud != null && longitud != null) {
            return listaDtos.stream()
                    .sorted(Comparator.comparing(
                            ReporteResponseDto::getDistanciaKm,
                            Comparator.nullsLast(Double::compareTo)
                    ))
                    .toList();
        }

        return listaDtos;
    }

    @Override
    @Transactional(readOnly = true)
    public ReporteResponseDto obtenerReportePorId(Long id) {
        Reporte reporte = reporteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el reporte con identificador: " + id));
        return mapearADto(reporte, null, null);
    }

    private ReporteResponseDto mapearADto(Reporte reporte, Double origenLat, Double origenLon) {
        Double distancia = null;
        if (origenLat != null && origenLon != null && reporte.getLatitud() != null && reporte.getLongitud() != null) {
            distancia = calcularDistanciaHaversine(origenLat, origenLon, reporte.getLatitud(), reporte.getLongitud());
        }

        return ReporteResponseDto.builder()
                .id(reporte.getId())
                .categoria(reporte.getCategoria())
                .municipio(reporte.getMunicipio())
                .referencia(reporte.getReferencia())
                .latitud(reporte.getLatitud())
                .longitud(reporte.getLongitud())
                .fechaReporte(reporte.getFechaReporte())
                .nombreUsuaria(reporte.getUsuario() != null ? reporte.getUsuario().getNombre() : "Anónima")
                .distanciaKm(distancia != null ? Math.round(distancia * 100.0) / 100.0 : null)
                .build();
    }

    /**
     * Calcula la distancia geográfica entre dos coordenadas usando la fórmula de Haversine.
     */
    private double calcularDistanciaHaversine(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return RADIO_TIERRA_KM * c;
    }
}
