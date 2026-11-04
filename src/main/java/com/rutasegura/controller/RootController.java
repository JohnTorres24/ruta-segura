package com.rutasegura.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

/**
 * Controlador raíz para comprobación de estado (Health Check) y redirección hacia Swagger UI.
 */
@RestController
public class RootController {

    @GetMapping("/")
    public RedirectView redireccionarSwagger() {
        return new RedirectView("/swagger-ui.html");
    }

    @GetMapping({"/api", "/api/health"})
    public ResponseEntity<Map<String, String>> healthCheck() {
        return ResponseEntity.ok(Map.of(
                "estado", "ACTIVO",
                "servicio", "Ruta Segura Backend API",
                "baseDatos", "PostgreSQL (Supabase)",
                "documentacion", "/swagger-ui.html"
        ));
    }
}
