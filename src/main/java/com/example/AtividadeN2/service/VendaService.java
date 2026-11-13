package com.example.AtividadeN2.service;

import com.example.AtividadeN2.dto.VendaRequestDTO;
import com.example.AtividadeN2.dto.VendaResponseDTO;
import com.example.AtividadeN2.entity.Produto;
import com.example.AtividadeN2.entity.Usuario;
import com.example.AtividadeN2.entity.Venda;
import com.example.AtividadeN2.repository.ProdutoRepository;
import com.example.AtividadeN2.repository.UsuarioRepository;
import com.example.AtividadeN2.repository.VendaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class VendaService {

    private final VendaRepository vendaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProdutoRepository produtoRepository;

    public VendaService(
            VendaRepository vendaRepository,
            UsuarioRepository usuarioRepository,
            ProdutoRepository produtoRepository) {

        this.vendaRepository = vendaRepository;
        this.usuarioRepository = usuarioRepository;
        this.produtoRepository = produtoRepository;
    }

    public VendaResponseDTO salvar(VendaRequestDTO dto) {

        Venda venda = VendaRequestDTO.toEntity(dto);

        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Produto produto = produtoRepository.findById(dto.produtoId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        venda.setUsuario(usuario);
        venda.setProduto(produto);

        Venda salva = vendaRepository.save(venda);

        return VendaResponseDTO.toDto(salva);
    }

    public List<VendaResponseDTO> listarTodas() {
        return vendaRepository.findAll()
                .stream()
                .map(VendaResponseDTO::toDto)
                .toList();
    }

    public VendaResponseDTO buscarPorId(UUID id) {

        Venda venda = vendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venda não encontrada"));

        return VendaResponseDTO.toDto(venda);
    }

    public void excluir(UUID id) {
        vendaRepository.deleteById(id);
    }


}
