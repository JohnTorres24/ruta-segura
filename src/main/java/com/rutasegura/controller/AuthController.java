package com.rutasegura.controller;

import com.rutasegura.dto.AuthResponseDto;
import com.rutasegura.dto.LoginRequestDto;
import com.rutasegura.dto.RegistroRequestDto;
import com.rutasegura.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para el registro y la autenticación de usuarias.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Endpoint para el registro de una nueva usuaria (admite /registro y /register).
     */
    @PostMapping({"/registro", "/register"})
    public ResponseEntity<AuthResponseDto> registrarUsuaria(@Valid @RequestBody RegistroRequestDto registroDto) {
        AuthResponseDto respuesta = authService.registrar(registroDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    /**
     * Endpoint para el inicio de sesión y obtención del token JWT.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> iniciarSesion(@Valid @RequestBody LoginRequestDto loginDto) {
        AuthResponseDto respuesta = authService.login(loginDto);
        return ResponseEntity.ok(respuesta);
    }

    /**
     * Endpoint protegido para consultar el perfil de la usuaria autenticada.
     */
    @org.springframework.web.bind.annotation.GetMapping("/me")
    public ResponseEntity<com.rutasegura.dto.UsuarioResponseDto> obtenerMiPerfil(
            org.springframework.security.core.Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "No se encontró la identidad de la usuaria autenticada.");
        }
        com.rutasegura.dto.UsuarioResponseDto perfil = authService.obtenerPerfil(authentication.getName());
        return ResponseEntity.ok(perfil);
    }
}
