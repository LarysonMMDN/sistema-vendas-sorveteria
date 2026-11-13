package api;

import com.example.AtividadeN2.AtividadeN2Application;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;


@SpringBootTest(
        classes = AtividadeN2Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class CategoriaApiTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void configurarPorta() {
        RestAssured.port = port;
    }

    @Test
    void deveCadastrarCategoriaPelaApi() {

        String categoriaJson = """
                {
                    "nome": "Categoria API Teste",
                    "descricao": "Categoria criada pelo teste de API",
                    "tipo": "Alimento",
                    "status": true
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(categoriaJson)
                .when()
                .post("/categorias")
                .then()
                .statusCode(201)
                .body("nome", equalTo("Categoria API Teste"))
                .body("tipo", equalTo("Alimento"))
                .body("status", equalTo(true));
    }

    @Test
    void deveListarCategoriasPelaApi() {

        given()
                .contentType(ContentType.JSON)
                .when()
                .request(Method.GET, "/categorias")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$", instanceOf(java.util.List.class));
    }
}

