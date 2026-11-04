package com.rutasegura.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador global para captura y formateo uniforme de excepciones en la API.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> manejarResourceNotFound(
            ResourceNotFoundException ex, HttpServletRequest request) {
        ErrorResponseDto respuesta = ErrorResponseDto.builder()
                .marcaTiempo(LocalDateTime.now())
                .codigoEstado(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .mensaje(ex.getMessage())
                .ruta(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseDto> manejarBadRequest(
            BadRequestException ex, HttpServletRequest request) {
        ErrorResponseDto respuesta = ErrorResponseDto.builder()
                .marcaTiempo(LocalDateTime.now())
                .codigoEstado(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .mensaje(ex.getMessage())
                .ruta(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponseDto> manejarConflict(
            ConflictException ex, HttpServletRequest request) {
        ErrorResponseDto respuesta = ErrorResponseDto.builder()
                .marcaTiempo(LocalDateTime.now())
                .codigoEstado(HttpStatus.CONFLICT.value())
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .mensaje(ex.getMessage())
                .ruta(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDto> manejarBadCredentials(
            BadCredentialsException ex, HttpServletRequest request) {
        ErrorResponseDto respuesta = ErrorResponseDto.builder()
                .marcaTiempo(LocalDateTime.now())
                .codigoEstado(HttpStatus.UNAUTHORIZED.value())
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .mensaje("Credenciales de acceso inválidas. Verifique su correo o contraseña.")
                .ruta(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDto> manejarAccessDenied(
            AccessDeniedException ex, HttpServletRequest request) {
        ErrorResponseDto respuesta = ErrorResponseDto.builder()
                .marcaTiempo(LocalDateTime.now())
                .codigoEstado(HttpStatus.FORBIDDEN.value())
                .error(HttpStatus.FORBIDDEN.getReasonPhrase())
                .mensaje("Acceso denegado: no cuenta con los permisos necesarios.")
                .ruta(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(respuesta);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> manejarValidacion(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errores = new HashMap<>();
        for (FieldError campoError : ex.getBindingResult().getFieldErrors()) {
            errores.put(campoError.getField(), campoError.getDefaultMessage());
        }

        ErrorResponseDto respuesta = ErrorResponseDto.builder()
                .marcaTiempo(LocalDateTime.now())
                .codigoEstado(HttpStatus.BAD_REQUEST.value())
                .error("Error de validación")
                .mensaje("Uno o más campos presentan valores inválidos.")
                .ruta(request.getRequestURI())
                .detallesValidacion(errores)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> manejarExcepcionGeneral(
            Exception ex, HttpServletRequest request) {
        ErrorResponseDto respuesta = ErrorResponseDto.builder()
                .marcaTiempo(LocalDateTime.now())
                .codigoEstado(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .mensaje("Ocurrió un error interno en el servidor: " + ex.getMessage())
                .ruta(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
    }
}
