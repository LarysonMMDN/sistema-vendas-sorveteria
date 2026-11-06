package com.example.AtividadeN2.dto;

import java.util.UUID;

public record VendaRequestDTO(
        String formaPagamento,
        UUID usuarioId,
        UUID produtoId
) {}
