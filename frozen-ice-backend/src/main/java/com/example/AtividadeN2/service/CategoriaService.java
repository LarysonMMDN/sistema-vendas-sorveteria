package com.example.AtividadeN2.service;

import com.example.AtividadeN2.dto.CategoriaRequestDTO;
import com.example.AtividadeN2.dto.CategoriaResponseDTO;
import com.example.AtividadeN2.entity.Categoria;
import com.example.AtividadeN2.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoriaService {


    private final CategoriaRepository categoriaRepository;

    public CategoriaResponseDTO cadastrar(CategoriaRequestDTO dto){

        Categoria categoria = CategoriaRequestDTO.toEntity(dto);

        Categoria categoriaSalva = categoriaRepository.save(categoria);

        return CategoriaResponseDTO.toDto(categoriaSalva);
    }

    public CategoriaResponseDTO buscarPorId(UUID categoriaId) {

        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        return CategoriaResponseDTO.toDto(categoria);
    }

    public List<CategoriaResponseDTO> listarCategorias() {

        return categoriaRepository.findAll()
                .stream()
                .map(CategoriaResponseDTO::toDto)
                .toList();
    }

    @Transactional
    public CategoriaResponseDTO atualizar(
            UUID categoriaId,
            CategoriaRequestDTO dto) {

        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        if (Boolean.TRUE.equals(categoria.getStatus())
                && Boolean.FALSE.equals(dto.status())
                && !categoria.getProdutos().isEmpty()) {

            throw new RuntimeException(
                    "Não é possível desativar uma categoria que possui produtos cadastrados"
            );
        }


        categoria.setNome(dto.nome());
        categoria.setDescricao(dto.descricao());
        categoria.setTipo(dto.tipo());
        categoria.setStatus(dto.status());

        Categoria categoriaAtualizada = categoriaRepository.save(categoria);

        return CategoriaResponseDTO.toDto(categoriaAtualizada);
    }

    public void deletar(UUID categoriaId) {

        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        categoriaRepository.delete(categoria);
    }
}
