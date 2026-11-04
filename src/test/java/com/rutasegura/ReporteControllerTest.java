package com.rutasegura;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rutasegura.controller.ReporteController;
import com.rutasegura.dto.ReporteCreateDto;
import com.rutasegura.dto.ReporteResponseDto;
import com.rutasegura.model.CategoriaRiesgo;
import com.rutasegura.security.JwtAuthenticationEntryPoint;
import com.rutasegura.security.JwtAuthenticationFilter;
import com.rutasegura.security.JwtTokenProvider;
import com.rutasegura.service.ReporteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReporteController.class)
@AutoConfigureMockMvc(addFilters = false)
class ReporteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ReporteService reporteService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    private JwtAuthenticationEntryPoint authenticationEntryPoint;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    @DisplayName("Debe listar reportes de riesgo públicamente")
    void debeListarReportesPublicamente() throws Exception {
        ReporteResponseDto reporte = ReporteResponseDto.builder()
                .id(1L)
                .categoria(CategoriaRiesgo.ILUMINACION_DEFICIENTE)
                .municipio("Soacha")
                .referencia("Parque principal junto a la estación")
                .fechaReporte(LocalDateTime.now())
                .nombreUsuaria("Ana Gómez")
                .build();

        when(reporteService.listarReportes(any(), any(), any(), any())).thenReturn(List.of(reporte));

        mockMvc.perform(get("/api/reportes")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].categoria").value("ILUMINACION_DEFICIENTE"))
                .andExpect(jsonPath("$[0].municipio").value("Soacha"));
    }

    @Test
    @WithMockUser(username = "usuaria@ejemplo.com")
    @DisplayName("Debe registrar un nuevo reporte rápido cuando la usuaria está autenticada")
    void debeCrearReporteConUsuariaAutenticada() throws Exception {
        ReporteCreateDto createDto = ReporteCreateDto.builder()
                .categoria(CategoriaRiesgo.ACOSO)
                .municipio("Bogotá")
                .referencia("Calle 72 con Carrera 11")
                .latitud(4.6582)
                .longitud(-74.0573)
                .build();

        ReporteResponseDto response = ReporteResponseDto.builder()
                .id(10L)
                .categoria(CategoriaRiesgo.ACOSO)
                .municipio("Bogotá")
                .referencia("Calle 72 con Carrera 11")
                .latitud(4.6582)
                .longitud(-74.0573)
                .fechaReporte(LocalDateTime.now())
                .nombreUsuaria("Usuaria Verificada")
                .build();

        when(reporteService.crearReporte(any(ReporteCreateDto.class), eq("usuaria@ejemplo.com")))
                .thenReturn(response);

        mockMvc.perform(post("/api/reportes")
                        .principal(new org.springframework.security.authentication.UsernamePasswordAuthenticationToken("usuaria@ejemplo.com", null))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.categoria").value("ACOSO"))
                .andExpect(jsonPath("$.municipio").value("Bogotá"));
    }
}
