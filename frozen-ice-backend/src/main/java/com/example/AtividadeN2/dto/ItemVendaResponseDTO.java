package com.example.AtividadeN2.dto;

import com.example.AtividadeN2.entity.ItemVenda;

import java.math.BigDecimal;
import java.util.UUID;

public record ItemVendaResponseDTO(

        UUID produtoId,
        String produtoNome,
        Integer quantidade,
        BigDecimal precoUnitario,
        BigDecimal subtotal
) {
    public static ItemVendaResponseDTO toDto(ItemVenda item) {

        BigDecimal subtotal = item.getPrecoUnitario()
                .multiply(BigDecimal.valueOf(item.getQuantidade()));

        return new ItemVendaResponseDTO(
                item.getProduto().getId(),
                item.getProduto().getNome(),
                item.getQuantidade(),
                item.getPrecoUnitario(),
                subtotal
        );
    }
}
