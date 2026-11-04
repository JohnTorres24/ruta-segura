package com.rutasegura.config;

import com.rutasegura.model.CategoriaRiesgo;
import com.rutasegura.model.Reporte;
import com.rutasegura.model.Rol;
import com.rutasegura.model.Usuario;
import com.rutasegura.repository.ReporteRepository;
import com.rutasegura.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Inicializador de datos semilla para facilitar el desarrollo y pruebas del equipo.
 */
@Component
@Profile("!test")
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final ReporteRepository reporteRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() == 0) {
            log.info("Inicializando datos de prueba en la base de datos...");

            Usuario usuariaDemo = Usuario.builder()
                    .nombre("Usuaria Demostración")
                    .email("demo@rutasegura.com")
                    .password(passwordEncoder.encode("demo123456"))
                    .rol(Rol.ROLE_USER)
                    .build();

            Usuario usuariaGuardada = usuarioRepository.save(usuariaDemo);

            Reporte rep1 = Reporte.builder()
                    .categoria(CategoriaRiesgo.ILUMINACION_DEFICIENTE)
                    .municipio("Soacha")
                    .referencia("Transversal 15 con Calle 8, luminarias apagadas en todo el tramo")
                    .latitud(4.5821)
                    .longitud(-74.2185)
                    .usuario(usuariaGuardada)
                    .build();

            Reporte rep2 = Reporte.builder()
                    .categoria(CategoriaRiesgo.ACOSO)
                    .municipio("Bogotá")
                    .referencia("Estación TransMilenio Calle 72, salida oriental sin presencia policial")
                    .latitud(4.6582)
                    .longitud(-74.0573)
                    .usuario(usuariaGuardada)
                    .build();

            Reporte rep3 = Reporte.builder()
                    .categoria(CategoriaRiesgo.ZONA_DESOLADA)
                    .municipio("Chía")
                    .referencia("Sendero peatonal del Río Frío, sector sin comercio ni vigilancia")
                    .latitud(4.8622)
                    .longitud(-74.0589)
                    .usuario(usuariaGuardada)
                    .build();

            Reporte rep4 = Reporte.builder()
                    .categoria(CategoriaRiesgo.SOSPECHOSO)
                    .municipio("Facatativá")
                    .referencia("Inmediaciones del Parque Santa Rita, esquina con callejón ciego")
                    .latitud(4.8143)
                    .longitud(-74.3541)
                    .usuario(usuariaGuardada)
                    .build();

            reporteRepository.saveAll(List.of(rep1, rep2, rep3, rep4));
            log.info("Datos semilla creados con éxito (Usuaria demo: demo@rutasegura.com / Clave: demo123456)");
        }
    }
}
