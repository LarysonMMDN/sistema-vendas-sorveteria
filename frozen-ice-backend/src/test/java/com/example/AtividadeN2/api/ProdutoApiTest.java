package com.example.AtividadeN2.api;

import com.example.AtividadeN2.entity.Categoria;
import com.example.AtividadeN2.entity.Produto;
import com.example.AtividadeN2.repository.CategoriaRepository;
import com.example.AtividadeN2.repository.ProdutoRepository;

import io.restassured.RestAssured;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ProdutoApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    private Categoria categoria;

    @BeforeEach
    void configurar() {

        RestAssured.port = port;

        // Limpa os dados de teste anteriores
        produtoRepository.deleteAll();
        categoriaRepository.deleteAll();

        // Cria uma categoria ativa
        categoria = new Categoria();
        categoria.setNome("Sorvetes");
        categoria.setDescricao("Categoria de sorvetes");
        categoria.setTipo("ALIMENTO");
        categoria.setStatus(true);

        categoria = categoriaRepository.save(categoria);
    }

    // TESTE 1: Listar produtos pela API
    @Test
    void deveListarProdutosPelaApi() {

        // Cria um produto para consulta
        Produto produto = new Produto();
        produto.setNome("Sorvete de chocolate");
        produto.setPreco(new java.math.BigDecimal("10.00"));
        produto.setSabor("Chocolate");
        produto.setEstoque(20);
        produto.setCategoria(categoria);

        produtoRepository.save(produto);

        given()
                .when()
                .get("/api/produtos")
                .then()
                .statusCode(200)
                .body("size()", equalTo(1))
                .body("[0].nome", equalTo("Sorvete de chocolate"))
                .body("[0].sabor", equalTo("Chocolate"))
                .body("[0].estoque", equalTo(20));
    }

    // TESTE 2: Cadastrar produto pela API
    @Test
    void deveCadastrarProdutoPelaApi() {

        String json = """
                {
                    "nome": "Sorvete de morango",
                    "preco": 12.50,
                    "sabor": "Morango",
                    "estoque": 15,
                    "categoriaId": "%s"
                }
                """.formatted(categoria.getId());

        given()
                .contentType("application/json")
                .body(json)
                .when()
                .post("/api/produtos")
                .then()
                .statusCode(200)
                .body("nome", equalTo("Sorvete de morango"))
                .body("sabor", equalTo("Morango"))
                .body("estoque", equalTo(15))
                .body("categoriaNome", equalTo("Sorvetes"))
                .body("categoriaId", equalTo(categoria.getId().toString()));
    }
}