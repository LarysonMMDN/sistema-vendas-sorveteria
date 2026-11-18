
package com.example.AtividadeN2.unit;

import com.example.AtividadeN2.dto.*;
import com.example.AtividadeN2.entity.*;
import com.example.AtividadeN2.repository.*;
import com.example.AtividadeN2.service.VendaService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VendaServiceTest {

    @Mock
    private VendaRepository vendaRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private VendaService vendaService;

    private Cliente cliente;
    private Usuario usuario;
    private Produto produto;

    private UUID clienteId;
    private UUID usuarioId;
    private UUID produtoId;

    @BeforeEach
    void configurar() {

        clienteId = UUID.randomUUID();
        usuarioId = UUID.randomUUID();
        produtoId = UUID.randomUUID();

        cliente = new Cliente();
        cliente.setId(clienteId);
        cliente.setNome("João");

        usuario = new Usuario();
        usuario.setId(usuarioId);
        usuario.setNome("Administrador");

        produto = new Produto();
        produto.setId(produtoId);
        produto.setNome("Sorvete de chocolate");
        produto.setPreco(new BigDecimal("10.00"));
        produto.setEstoque(10);
    }

    // TESTE 1: Venda realizada com sucesso
    @Test
    void deveSalvarVendaComSucesso() {

        ItemVendaRequestDTO item =
                new ItemVendaRequestDTO(produtoId, 3);

        VendaRequestDTO dto = new VendaRequestDTO(
                "PIX",
                clienteId,
                usuarioId,
                List.of(item)
        );

        when(clienteRepository.findById(clienteId))
                .thenReturn(Optional.of(cliente));

        when(usuarioRepository.findById(usuarioId))
                .thenReturn(Optional.of(usuario));

        when(produtoRepository.findById(produtoId))
                .thenReturn(Optional.of(produto));

        when(vendaRepository.save(any(Venda.class)))
                .thenAnswer(invocation -> {
                    Venda venda = invocation.getArgument(0);
                    venda.setId(UUID.randomUUID());
                    return venda;
                });

        VendaResponseDTO resposta = vendaService.salvar(dto);

        assertNotNull(resposta);
        assertEquals("FINALIZADA", resposta.status());
        assertEquals(new BigDecimal("30.00"), resposta.valorTotal());
        assertEquals(7, produto.getEstoque());
        assertEquals(1, resposta.itens().size());

        verify(vendaRepository, times(1))
                .save(any(Venda.class));
    }

    // TESTE 2: Estoque insuficiente
    @Test
    void deveImpedirVendaComEstoqueInsuficiente() {

        produto.setEstoque(2);

        ItemVendaRequestDTO item =
                new ItemVendaRequestDTO(produtoId, 5);

        VendaRequestDTO dto = new VendaRequestDTO(
                "DINHEIRO",
                clienteId,
                usuarioId,
                List.of(item)
        );

        when(clienteRepository.findById(clienteId))
                .thenReturn(Optional.of(cliente));

        when(usuarioRepository.findById(usuarioId))
                .thenReturn(Optional.of(usuario));

        when(produtoRepository.findById(produtoId))
                .thenReturn(Optional.of(produto));

        RuntimeException erro = assertThrows(
                RuntimeException.class,
                () -> vendaService.salvar(dto)
        );

        assertTrue(erro.getMessage()
                .contains("Estoque insuficiente"));

        assertEquals(2, produto.getEstoque());

        verify(vendaRepository, never())
                .save(any(Venda.class));
    }

    // TESTE 3: Produto não encontrado
    @Test
    void deveImpedirVendaQuandoProdutoNaoExiste() {

        ItemVendaRequestDTO item =
                new ItemVendaRequestDTO(produtoId, 2);

        VendaRequestDTO dto = new VendaRequestDTO(
                "CARTAO",
                clienteId,
                usuarioId,
                List.of(item)
        );

        when(clienteRepository.findById(clienteId))
                .thenReturn(Optional.of(cliente));

        when(usuarioRepository.findById(usuarioId))
                .thenReturn(Optional.of(usuario));

        when(produtoRepository.findById(produtoId))
                .thenReturn(Optional.empty());

        RuntimeException erro = assertThrows(
                RuntimeException.class,
                () -> vendaService.salvar(dto)
        );

        assertEquals("Produto não encontrado", erro.getMessage());

        verify(vendaRepository, never())
                .save(any(Venda.class));
    }
}
