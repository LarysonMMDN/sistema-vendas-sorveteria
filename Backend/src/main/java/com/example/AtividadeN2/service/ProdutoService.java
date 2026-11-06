package com.example.AtividadeN2.service;

import com.example.AtividadeN2.dto.ProdutoRequestDTO;
import com.example.AtividadeN2.entity.Categoria;
import com.example.AtividadeN2.entity.Produto;
import com.example.AtividadeN2.repository.CategoriaRepository;
import com.example.AtividadeN2.repository.ProdutoRepository;

public class ProdutoService {
    private ProdutoRepository produtoRepository;
    private CategoriaRepository categoriaRepository;

    public ProdutoService(ProdutoRepository produtoRepository, CategoriaRepository categoriaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public Produto cadastrar(ProdutoRequestDTO dto){
        Categoria categoria = categoriaRepository.findById(dto.categoria().getId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        Produto produto = ProdutoRequestDTO.toEntity(dto);
        produto.setCategoria(categoria);

        return produtoRepository.save(produto);
    }
}
