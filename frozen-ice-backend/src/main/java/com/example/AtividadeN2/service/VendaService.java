package com.example.AtividadeN2.service;

import com.example.AtividadeN2.dto.VendaRequestDTO;
import com.example.AtividadeN2.dto.VendaResponseDTO;
import com.example.AtividadeN2.entity.*;
import com.example.AtividadeN2.repository.ClienteRepository;
import com.example.AtividadeN2.repository.ProdutoRepository;
import com.example.AtividadeN2.repository.UsuarioRepository;
import com.example.AtividadeN2.repository.VendaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class VendaService {

    private final VendaRepository vendaRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    private final UsuarioRepository usuarioRepository;

    public VendaService(
            VendaRepository vendaRepository,
            ClienteRepository clienteRepository,
            ProdutoRepository produtoRepository,
            UsuarioRepository usuarioRepository) {

        this.vendaRepository = vendaRepository;
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public VendaResponseDTO salvar(VendaRequestDTO dto) {

        Cliente cliente = clienteRepository.findById(dto.clienteId())
                .orElseThrow(() ->
                        new RuntimeException("Cliente não encontrado"));

        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() ->
                        new RuntimeException("Usuário não encontrado"));

        Venda venda = new Venda();

        venda.setCliente(cliente);
        venda.setUsuario(usuario);
        venda.setFormaPagamento(dto.formaPagamento());
        venda.setStatus("FINALIZADA");
        venda.setDataVenda(LocalDateTime.now());

        BigDecimal total = BigDecimal.ZERO;

        for (var itemDTO : dto.itens()) {

            Produto produto = produtoRepository.findById(itemDTO.produtoId())
                    .orElseThrow(() ->
                            new RuntimeException("Produto não encontrado"));

            Integer quantidade = itemDTO.quantidade();

            if (produto.getEstoque() < quantidade) {
                throw new RuntimeException(
                        "Estoque insuficiente para o produto: "
                                + produto.getNome()
                );
            }

            BigDecimal preco = produto.getPreco();

            if (preco == null) {
                throw new RuntimeException(
                        "Produto sem preço cadastrado: " + produto.getNome()
                );
            }

            ItemVenda item = new ItemVenda();

            item.setVenda(venda);
            item.setProduto(produto);
            item.setQuantidade(quantidade);
            item.setPrecoUnitario(preco);

            venda.getItens().add(item);

            produto.setEstoque(produto.getEstoque() - quantidade);

            total = total.add(
                    preco.multiply(BigDecimal.valueOf(quantidade))
            );
        }

        venda.setValorTotal(total);

        Venda salva = vendaRepository.save(venda);

        return VendaResponseDTO.toDto(salva);
    }

    @Transactional(readOnly = true)
    public List<VendaResponseDTO> listarTodas() {

        return vendaRepository.findAll()
                .stream()
                .map(VendaResponseDTO::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public VendaResponseDTO buscarPorId(UUID id) {

        Venda venda = vendaRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Venda não encontrada"));

        return VendaResponseDTO.toDto(venda);
    }

    public void excluir(UUID id) {
        vendaRepository.deleteById(id);
    }
}