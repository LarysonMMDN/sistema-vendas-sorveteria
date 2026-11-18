package com.example.AtividadeN2.dto;

import com.example.AtividadeN2.entity.Categoria;
import com.example.AtividadeN2.entity.Produto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.UUID;

public record ProdutoRequestDTO(

        @NotBlank(message = "Nome não pode ser vazio")
        String nome,

        @NotNull(message = "Preço não pode estar vazio")
        @Positive(message = "Preço deve ser maior que zero")
        BigDecimal preco,

        String sabor,

        @NotNull(message = "Estoque deve ser informado")
        @PositiveOrZero(message = "Estoque não pode ser negativo")
        Integer estoque,

        @NotNull(message = "Categoria deve ser informada")
        UUID categoriaId
){
    public static Produto toEntity(ProdutoRequestDTO dto) {
        Produto produto = new Produto();

        produto.setNome(dto.nome());
        produto.setPreco(dto.preco());
        produto.setSabor(dto.sabor());
        produto.setEstoque(dto.estoque());

        return produto;
    }
}
