
package com.example.AtividadeN2.e2e;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.JavascriptExecutor;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

class LoginE2ETest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void configurar() {

        driver = new ChromeDriver();

        driver.manage().window().maximize();

        wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(15)
        );

        driver.get("http://localhost:5173");
    }

    @Test
    void deveRealizarLoginComSucesso() {

        // Localiza o campo de e-mail
        WebElement campoEmail = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("input[type='email']")
                )
        );

        campoEmail.sendKeys("admin@email.com");

        // Localiza o campo de senha
        WebElement campoSenha = driver.findElement(
                By.cssSelector("input[type='password']")
        );

        campoSenha.sendKeys("admin123");

        // Clica no botão de entrar
        driver.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        // Aguarda o carregamento do painel principal
        WebElement painel = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector(".app-container")
                )
        );

        // Verifica se o login foi realizado
        assertTrue(painel.isDisplayed());
    }

    @Test
    void deveCadastrarProdutoPelaInterface() {

        // 1. Fazer login
        driver.findElement(
                By.cssSelector("input[type='email']")
        ).sendKeys("admin@email.com");

        driver.findElement(
                By.cssSelector("input[type='password']")
        ).sendKeys("admin123");

        driver.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        // 2. Aguardar o painel principal
        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector(".app-container")
                )
        );

        // 3. Acessar a tela de produtos
        wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[contains(@class,'menu-item') and .//span[text()='Produtos']]")
                )
        ).click();

        // 4. Abrir o formulário de cadastro
        wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[contains(.,'Novo produto')]")
                )
        ).click();

        // 5. Preencher os dados do produto
        String nomeProduto = "Sorvete E2E Selenium";

        driver.findElement(
                By.name("nome")
        ).sendKeys(nomeProduto);

        driver.findElement(
                By.name("sabor")
        ).sendKeys("Chocolate");

        driver.findElement(
                By.name("preco")
        ).sendKeys("15.50");

        driver.findElement(
                By.name("estoque")
        ).sendKeys("30");

        // 6. Selecionar uma categoria ativa
        WebElement categoria = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.name("categoriaId")
                )
        );

        wait.until(driver ->
                driver.findElements(
                        By.cssSelector(
                                "select[name='categoriaId'] option:not([value='']):not(:disabled)"
                        )
                ).size() > 0
        );

        org.openqa.selenium.support.ui.Select selectCategoria =
                new org.openqa.selenium.support.ui.Select(categoria);

        selectCategoria.selectByIndex(1);

        // 7. Cadastrar o produto
        WebElement botaoSalvar = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("button.save-product-btn")
                )
        );

// Centraliza o botão na tela
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center', inline: 'center'});",
                botaoSalvar
        );

// Aguarda o botão ficar habilitado
        wait.until(ExpectedConditions.elementToBeClickable(botaoSalvar));

// Verifica a posição do botão
        Object diagnostico = ((JavascriptExecutor) driver).executeScript(
                "const r = arguments[0].getBoundingClientRect();" +
                        "return 'Top: ' + r.top + " +
                        "' | Bottom: ' + r.bottom + " +
                        "' | Altura da tela: ' + window.innerHeight;",
                botaoSalvar
        );

        System.out.println("POSIÇÃO DO BOTÃO: " + diagnostico);

// Move até o botão e tenta clicar
        new org.openqa.selenium.interactions.Actions(driver)
                .moveToElement(botaoSalvar)
                .click()
                .perform();

        // 8. Verificar se apareceu na listagem
        wait.until(driver ->
                ((JavascriptExecutor) driver).executeScript(
                        "return document.readyState"
                ).equals("complete")
        );

// Mostrar mensagens que apareceram na tela
        System.out.println("TEXTO DA PÁGINA:");
        System.out.println(driver.findElement(By.tagName("body")).getText());

// Verificar se o produto apareceu em algum lugar
        boolean produtoExiste = driver.getPageSource()
                .contains(nomeProduto);

        System.out.println("PRODUTO ENCONTRADO NA PÁGINA: " + produtoExiste);

// Verificar mensagens de erro
        System.out.println("ALERTAS:");
        driver.findElements(By.cssSelector(
                ".error, .alert, .toast, [role='alert']"
        )).forEach(elemento ->
                System.out.println(elemento.getText())
        );

        assertTrue(produtoExiste,
                "O produto não apareceu na página após o cadastro.");
    }

    @AfterEach
    void finalizar() {

        if (driver != null) {
            driver.quit();
        }
    }
}
