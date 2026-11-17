package com.example.AtividadeN2.dto;

import com.example.AtividadeN2.entity.Produto;
import com.example.AtividadeN2.entity.Usuario;
import com.example.AtividadeN2.entity.Venda;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record VendaRequestDTO(

        @NotBlank(message = "Forma de pagamento não pode ser vazia")
        String formaPagamento,

        @NotNull(message = "Valor total não pode ser vazio")
        @Positive(message = "Valor total deve ser maior que zero")
        BigDecimal valorTotal,

        @NotBlank(message = "Status não pode ser vazio")
        String status,

        @NotNull(message = "Usuário deve ser informado")
        UUID usuarioId,

        @NotNull(message = "Produto deve ser informado")
        UUID produtoId
) {
    public static Venda toEntity(VendaRequestDTO dto){
        Venda venda = new Venda();

        venda.setFormaPagamento(dto.formaPagamento());
        venda.setValorTotal(dto.valorTotal());
        venda.setStatus(dto.status());

        return venda;
    }
}
