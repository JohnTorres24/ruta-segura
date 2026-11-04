package com.rutasegura.service;

import com.rutasegura.dto.AuthResponseDto;
import com.rutasegura.dto.LoginRequestDto;
import com.rutasegura.dto.RegistroRequestDto;
import com.rutasegura.exception.BadRequestException;
import com.rutasegura.exception.ResourceNotFoundException;
import com.rutasegura.model.Rol;
import com.rutasegura.model.Usuario;
import com.rutasegura.repository.UsuarioRepository;
import com.rutasegura.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación de los servicios de registro e inicio de sesión.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    @Override
    @Transactional
    public AuthResponseDto registrar(RegistroRequestDto registroDto) {
        if (usuarioRepository.existsByEmail(registroDto.getEmail())) {
            throw new com.rutasegura.exception.ConflictException("El correo electrónico ingresado ya se encuentra registrado en el sistema.");
        }

        Usuario usuario = Usuario.builder()
                .nombre(registroDto.getNombre())
                .email(registroDto.getEmail().trim().toLowerCase())
                .password(passwordEncoder.encode(registroDto.getPassword()))
                .rol(Rol.ROLE_USER)
                .build();

        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        String token = tokenProvider.generarTokenDesdeEmail(usuarioGuardado.getEmail());

        return AuthResponseDto.builder()
                .token(token)
                .tipo("Bearer")
                .email(usuarioGuardado.getEmail())
                .nombre(usuarioGuardado.getNombre())
                .rol(usuarioGuardado.getRol().name())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponseDto login(LoginRequestDto loginDto) {
        String emailNormalizado = loginDto.getEmail().trim().toLowerCase();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(emailNormalizado, loginDto.getPassword())
        );

        Usuario usuario = usuarioRepository.findByEmail(emailNormalizado)
                .orElseThrow(() -> new ResourceNotFoundException("Usuaria no encontrada con el correo proporcionado."));

        String token = tokenProvider.generarToken(authentication);

        return AuthResponseDto.builder()
                .token(token)
                .tipo("Bearer")
                .email(usuario.getEmail())
                .nombre(usuario.getNombre())
                .rol(usuario.getRol().name())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public com.rutasegura.dto.UsuarioResponseDto obtenerPerfil(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuaria no encontrada con el correo: " + email));

        return com.rutasegura.dto.UsuarioResponseDto.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .rol(usuario.getRol())
                .fechaCreacion(usuario.getFechaCreacion())
                .build();
    }
}
