package com.example.AtividadeN2.entity;

import com.example.AtividadeN2.dto.ProdutoRequestDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String nome;
    private BigDecimal preco;
    private String sabor;
    private Categoria categoria;

    public static Produto toEntity(ProdutoRequestDTO dto) {
        Produto produto = new Produto();

        produto.setNome(dto.nome());
        produto.setCategoria(dto.categoria());
        produto.setPreco(dto.preco());
        produto.setSabor(dto.sabor());

        return  produto;

    }
}
