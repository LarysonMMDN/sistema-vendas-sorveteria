package Unit;

import com.example.AtividadeN2.dto.CategoriaRequestDTO;
import com.example.AtividadeN2.dto.CategoriaResponseDTO;
import com.example.AtividadeN2.entity.Categoria;
import com.example.AtividadeN2.entity.Produto;
import com.example.AtividadeN2.repository.CategoriaRepository;
import com.example.AtividadeN2.service.CategoriaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    @Test
    void deveCadastrarCategoriaComSucesso() {

        CategoriaRequestDTO dto = new CategoriaRequestDTO(
                "Sorvetes",
                "Categoria de sorvetes",
                "Alimento",
                true
        );

        Categoria categoriaSalva = new Categoria();
        categoriaSalva.setId(UUID.randomUUID());
        categoriaSalva.setNome(dto.nome());
        categoriaSalva.setDescricao(dto.descricao());
        categoriaSalva.setTipo(dto.tipo());
        categoriaSalva.setStatus(dto.status());

        when(categoriaRepository.save(any(Categoria.class)))
                .thenReturn(categoriaSalva);

        CategoriaResponseDTO resposta = categoriaService.cadastrar(dto);

        assertNotNull(resposta);
        assertEquals("Sorvetes", resposta.nome());
        assertEquals("Alimento", resposta.tipo());
        assertTrue(resposta.status());

        verify(categoriaRepository, times(1)).save(any(Categoria.class));
    }

    @Test
    void naoDeveDesativarCategoriaQuePossuiProdutos() {

        UUID id = UUID.randomUUID();

        Categoria categoria = new Categoria();
        categoria.setId(id);
        categoria.setNome("Sorvetes");
        categoria.setDescricao("Categoria de sorvetes");
        categoria.setTipo("Alimento");
        categoria.setStatus(true);

        Produto produto = new Produto();
        produto.setNome("Chocolate");
        produto.setPreco(new BigDecimal("10.00"));
        produto.setSabor("Chocolate");
        produto.setCategoria(categoria);

        categoria.getProdutos().add(produto);

        CategoriaRequestDTO dto = new CategoriaRequestDTO(
                "Sorvetes",
                "Categoria de sorvetes",
                "Alimento",
                false
        );

        when(categoriaRepository.findById(id))
                .thenReturn(Optional.of(categoria));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> categoriaService.atualizar(id, dto)
        );

        assertEquals(
                "Não é possível desativar uma categoria que possui produtos cadastrados",
                exception.getMessage()
        );

        verify(categoriaRepository, never()).save(any(Categoria.class));
    }

    @Test
    void deveLancarExcecaoAoBuscarCategoriaInexistente() {

        UUID id = UUID.randomUUID();

        when(categoriaRepository.findById(id))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> categoriaService.buscarPorId(id)
        );

        assertEquals("Categoria não encontrada", exception.getMessage());

        verify(categoriaRepository, times(1)).findById(id);
    }
}

