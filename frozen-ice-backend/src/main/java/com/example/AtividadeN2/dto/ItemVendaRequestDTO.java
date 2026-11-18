package com.example.AtividadeN2.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record ItemVendaRequestDTO(
        @NotNull(message = "Produto deve ser informado")
        UUID produtoId,

        @NotNull(message = "Quantidade deve ser informada")
        @Positive(message = "Quantidade deve ser maior que zero")
        Integer quantidade

) {
}