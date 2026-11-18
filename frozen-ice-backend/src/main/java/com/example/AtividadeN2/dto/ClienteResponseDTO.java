package com.example.AtividadeN2.dto;

import java.util.UUID;

public record ClienteResponseDTO(

        UUID id,
        String nome,
        String cpf,
        String telefone,
        String email,
        String endereco

) {
}