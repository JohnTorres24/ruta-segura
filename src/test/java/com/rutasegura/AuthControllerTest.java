package com.rutasegura;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rutasegura.controller.AuthController;
import com.rutasegura.dto.AuthResponseDto;
import com.rutasegura.dto.LoginRequestDto;
import com.rutasegura.dto.RegistroRequestDto;
import com.rutasegura.security.JwtAuthenticationEntryPoint;
import com.rutasegura.security.JwtAuthenticationFilter;
import com.rutasegura.security.JwtTokenProvider;
import com.rutasegura.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    private JwtAuthenticationEntryPoint authenticationEntryPoint;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    @DisplayName("Debe registrar exitosamente a una usuaria y retornar token JWT")
    void debeRegistrarUsuariaExitosamente() throws Exception {
        RegistroRequestDto request = RegistroRequestDto.builder()
                .nombre("María Rodríguez")
                .email("maria@ejemplo.com")
                .password("segura123")
                .build();

        AuthResponseDto response = AuthResponseDto.builder()
                .token("mocked-jwt-token")
                .tipo("Bearer")
                .email("maria@ejemplo.com")
                .nombre("María Rodríguez")
                .rol("ROLE_USER")
                .build();

        when(authService.registrar(any(RegistroRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"))
                .andExpect(jsonPath("$.email").value("maria@ejemplo.com"))
                .andExpect(jsonPath("$.tipo").value("Bearer"));
    }

    @Test
    @DisplayName("Debe iniciar sesión correctamente y devolver el token JWT")
    void debeIniciarSesionCorrectamente() throws Exception {
        LoginRequestDto request = LoginRequestDto.builder()
                .email("maria@ejemplo.com")
                .password("segura123")
                .build();

        AuthResponseDto response = AuthResponseDto.builder()
                .token("mocked-jwt-token")
                .tipo("Bearer")
                .email("maria@ejemplo.com")
                .nombre("María Rodríguez")
                .rol("ROLE_USER")
                .build();

        when(authService.login(any(LoginRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"))
                .andExpect(jsonPath("$.email").value("maria@ejemplo.com"));
    }
}
