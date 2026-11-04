package com.rutasegura.service;

import com.rutasegura.dto.AuthResponseDto;
import com.rutasegura.dto.LoginRequestDto;
import com.rutasegura.dto.RegistroRequestDto;

/**
 * Contrato de servicios para el registro y autenticación de usuarias.
 */
public interface AuthService {

    AuthResponseDto registrar(RegistroRequestDto registroDto);

    AuthResponseDto login(LoginRequestDto loginDto);
}
