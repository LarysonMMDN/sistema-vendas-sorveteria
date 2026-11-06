package com.example.AtividadeN2.dto;

import java.util.UUID;
import java.time.LocalDateTime;

public record UsuarioResponseDTO(
        UUID id,
        String nome,
        String email,
        LocalDateTime dataCadastro
) {}
