package com.example.AtividadeN2.service;

import com.example.AtividadeN2.dto.LoginRequestDTO;
import com.example.AtividadeN2.dto.LoginResponseDTO;
import com.example.AtividadeN2.dto.UsuarioResponseDTO;
import com.example.AtividadeN2.entity.Usuario;
import com.example.AtividadeN2.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponseDTO login(LoginRequestDTO dto) {

        Usuario usuario = usuarioRepository.findByEmail(dto.email())
                .orElseThrow(() ->
                        new RuntimeException("E-mail ou senha incorretos")
                );

        if (!passwordEncoder.matches(dto.senha(), usuario.getSenha())) {
            throw new RuntimeException("E-mail ou senha incorretos");
        }

        String token = jwtService.gerarToken(usuario);

        return new LoginResponseDTO(
                token,
                UsuarioResponseDTO.toDto(usuario)
        );
    }
}
