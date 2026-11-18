package com.example.AtividadeN2.dto;

import com.example.AtividadeN2.entity.Venda;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record VendaResponseDTO(
        UUID id,
        String formaPagamento,
        BigDecimal valorTotal,
        String status,
        LocalDateTime dataVenda,
        UUID clienteId,
        UUID usuarioId,
        List<ItemVendaResponseDTO> itens
) {
    public static VendaResponseDTO toDto(Venda venda) {

        return new VendaResponseDTO(
                venda.getId(),
                venda.getFormaPagamento(),
                venda.getValorTotal(),
                venda.getStatus(),
                venda.getDataVenda(),
                venda.getCliente().getId(),
                venda.getUsuario().getId(),
                venda.getItens()
                        .stream()
                        .map(ItemVendaResponseDTO::toDto)
                        .toList()
        );
    }
}