package com.rutasegura.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Objeto de transferencia de datos con la respuesta de autenticación y el token JWT.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseDto {

    private String token;
    @Builder.Default
    private String tipo = "Bearer";
    private String email;
    private String nombre;
    private String rol;
}
