package com.example.AtividadeN2.Controller;

import com.example.AtividadeN2.dto.ProdutoRequestDTO;
import com.example.AtividadeN2.dto.ProdutoResponseDTO;
import com.example.AtividadeN2.entity.Produto;
import com.example.AtividadeN2.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;


    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> cadastrar(
            @Valid @RequestBody ProdutoRequestDTO dto) {

        Produto produto = produtoService.cadastrar(dto);

        return ResponseEntity.ok(ProdutoResponseDTO.toDto(produto));
    }

    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> listar() {

        List<ProdutoResponseDTO> produtos = produtoService.listarProdutos()
                .stream()
                .map(ProdutoResponseDTO::toDto)
                .toList();

        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> buscarPorId(
            @PathVariable UUID id) {

        Produto produto = produtoService.buscarPorId(id);

        return ResponseEntity.ok(ProdutoResponseDTO.toDto(produto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> atualizar(
            @PathVariable UUID id,
            @RequestBody ProdutoRequestDTO dto) {

        Produto produto = produtoService.atualizar(dto, id);

        return ResponseEntity.ok(ProdutoResponseDTO.toDto(produto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {

        produtoService.deletar(id);

        return ResponseEntity.noContent().build();
    }

}
