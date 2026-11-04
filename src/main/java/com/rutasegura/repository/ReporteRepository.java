package com.rutasegura.repository;

import com.rutasegura.model.CategoriaRiesgo;
import com.rutasegura.model.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para operaciones de persistencia sobre la entidad Reporte.
 */
@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {

    List<Reporte> findAllByOrderByFechaReporteDesc();

    List<Reporte> findByCategoriaOrderByFechaReporteDesc(CategoriaRiesgo categoria);

    List<Reporte> findByMunicipioIgnoreCaseOrderByFechaReporteDesc(String municipio);

    List<Reporte> findByCategoriaAndMunicipioIgnoreCaseOrderByFechaReporteDesc(CategoriaRiesgo categoria, String municipio);
}
