package com.example.AtividadeN2.dto;

import com.example.AtividadeN2.entity.Categoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoriaRequestDTO(

        @NotBlank(message = "Nome não pode ser vazio")
        String nome,

        String descricao,

        @NotBlank(message = "Tipo não pode ser vazio")
        String tipo,

        @NotNull(message = "Status deve ser informado")
        Boolean status
) {
    public static Categoria toEntity(CategoriaRequestDTO dto){
        Categoria categoria = new Categoria();

        categoria.setNome(dto.nome());
        categoria.setDescricao(dto.descricao());
        categoria.setTipo(dto.tipo());
        categoria.setStatus(dto.status());

        return categoria;
    }
}
