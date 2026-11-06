package com.example.AtividadeN2.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record VendaResponseDTO(
        UUID id,
        String formaPagamento,
        BigDecimal valorTotal,
        String status,
        String nomeUsuario,
        String nomeProduto
) {}
