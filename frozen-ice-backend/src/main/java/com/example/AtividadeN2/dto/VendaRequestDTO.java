package com.example.AtividadeN2.dto;

import com.example.AtividadeN2.entity.Produto;
import com.example.AtividadeN2.entity.Usuario;
import com.example.AtividadeN2.entity.Venda;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record VendaRequestDTO(

        @NotBlank(message = "Forma de pagamento não pode ser vazia")
        String formaPagamento,

        @NotNull(message = "Cliente deve ser informado")
        UUID clienteId,

        @NotNull(message = "Usuário deve ser informado")
        UUID usuarioId,

        @NotEmpty(message = "A venda precisa ter pelo menos um produto")
        List<@Valid ItemVendaRequestDTO> itens

) {
}