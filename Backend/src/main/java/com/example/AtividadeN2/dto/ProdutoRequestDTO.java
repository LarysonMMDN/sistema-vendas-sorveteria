package com.example.AtividadeN2.dto;

import com.example.AtividadeN2.entity.Categoria;

import java.math.BigDecimal;
import java.util.UUID;

public record ProdutoRequestDTO(
    String nome,
    BigDecimal preco,
    String sabor,
    Categoria categoria
){

}
