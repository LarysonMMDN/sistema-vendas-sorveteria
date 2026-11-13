package com.example.AtividadeN2.service;

import com.example.AtividadeN2.dto.ProdutoRequestDTO;
import com.example.AtividadeN2.entity.Categoria;
import com.example.AtividadeN2.entity.Produto;
import com.example.AtividadeN2.repository.CategoriaRepository;
import com.example.AtividadeN2.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;

    public Produto cadastrar(ProdutoRequestDTO dto){

        Categoria categoria = categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        if (!Boolean.TRUE.equals(categoria.getStatus())) {
            throw new RuntimeException(
                    "Não é possível cadastrar produto em uma categoria desativada"
            );
        }

        Produto produto = ProdutoRequestDTO.toEntity(dto);

        produto.setCategoria(categoria);

        return produtoRepository.save(produto);
    }

    public Produto atualizar(ProdutoRequestDTO dto, UUID produtoId){


        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        Categoria categoria = categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        produto.setNome(dto.nome());
        produto.setSabor(dto.sabor());
        produto.setPreco(dto.preco());
        produto.setCategoria(categoria);

        return produtoRepository.save(produto);
    }

    public Produto buscarPorId(UUID produtoId){

        return produtoRepository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    public List<Produto> listarProdutos(){
        return produtoRepository.findAll();
    }

    public void deletar(UUID produtoId){

        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        produtoRepository.delete(produto);
    }
}
