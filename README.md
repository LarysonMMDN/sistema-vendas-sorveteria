# FROZEN-ICE ❄️

## 1. Integrantes do Grupo

- **Matheus Araujo**
- **Heitor Ferreira**
- **Laryson Martins**
- **Natan Vasconcelos**

## 2. Descrição da Proposta

O FROZEN-ICE é um sistema de gerenciamento desenvolvido para facilitar a administração de uma sorveteria, centralizando em uma única plataforma as principais atividades relacionadas ao funcionamento do estabelecimento.

A proposta surgiu da necessidade de organizar e otimizar processos como cadastro de produtos, controle de estoque, gerenciamento de clientes, acompanhamento de vendas e administração de usuários.

O sistema busca substituir controles manuais e descentralizados por uma solução digital integrada, proporcionando maior organização, agilidade e segurança no gerenciamento das informações.

A aplicação possui uma interface intuitiva e organizada, permitindo que os usuários acessem diferentes funcionalidades de acordo com suas necessidades e permissões.

### Principais funcionalidades

- **Dashboard:** apresenta uma visão geral das informações e movimentações do estabelecimento.
- **Gerenciamento de produtos:** permite cadastrar, consultar, editar e excluir produtos.
- **Controle de categorias:** possibilita organizar os produtos em diferentes categorias.
- **Controle de estoque:** permite acompanhar as quantidades disponíveis dos produtos.
- **Gerenciamento de clientes:** possibilita cadastrar e administrar os dados dos clientes.
- **Registro de vendas:** permite selecionar produtos, adicionar itens ao carrinho, identificar clientes e registrar vendas.
- **Histórico de vendas:** disponibiliza informações sobre as vendas realizadas.
- **Gerenciamento de usuários:** permite administrar os usuários que possuem acesso ao sistema.
- **Autenticação:** realiza o controle de acesso por meio de login e autenticação.
- **Notificações:** apresenta avisos relacionados às atividades do sistema.
- **Configurações:** disponibiliza opções de configuração da aplicação.

## 3. Tecnologias Utilizadas

O projeto foi desenvolvido utilizando uma arquitetura dividida entre frontend e backend, permitindo a separação entre a interface do usuário e as regras de negócio.

### Frontend

- **React:** biblioteca JavaScript utilizada para construção das interfaces.
- **Vite:** ferramenta utilizada para inicialização e execução do ambiente de desenvolvimento.
- **JavaScript:** linguagem utilizada no desenvolvimento da aplicação.
- **Framer Motion:** biblioteca utilizada para animações e transições da interface.
- **Lucide React:** biblioteca de ícones utilizada nos componentes visuais.
- **CSS:** utilizado para estilização e organização visual das páginas.

### Backend

- **Java:** linguagem utilizada no desenvolvimento das regras de negócio.
- **Spring Boot:** framework utilizado para construção da API e gerenciamento da aplicação.
- **Spring Data JPA:** utilizado para persistência e gerenciamento dos dados.
- **Spring Security:** utilizado para controle de acesso e segurança.
- **JWT (JSON Web Token):** utilizado no processo de autenticação.
- **Maven:** ferramenta utilizada para gerenciamento de dependências e execução do projeto.

### Testes

- **JUnit:** utilizado para execução de testes automatizados.
- **Selenium:** utilizado para testes de interface e fluxos de navegação.
- **Mockito:** utilizado para criação de objetos simulados durante os testes.

### Controle de versão

- **Git:** utilizado para controle de versão do código-fonte.
- **GitHub:** utilizado para hospedagem do repositório e colaboração entre os integrantes.

## 4. Estrutura do Projeto

O projeto está organizado em duas aplicações principais:

```text
frozen-ice-main/
│
├── frozen-ice-backend/
│   ├── src/
│   │   ├── main/
│   │   │   └── java/
│   │   └── test/
│   └── pom.xml
│
├── frozen-ice-frontend/
│   ├── src/
│   │   ├── pages/
│   │   ├── services/
│   │   └── components/
│   ├── package.json
│   └── vite.config.js
│
└── README.md
```

## 5. Requisitos para Execução

Antes de executar o projeto, é necessário possuir:

- Java JDK instalado e configurado.
- Node.js e npm instalados.
- IntelliJ IDEA ou outra IDE compatível com Java.
- Visual Studio Code ou outra IDE para o frontend.
- Banco de dados configurado conforme as propriedades do backend.

## 6. Instruções para Execução do Ambiente

### 6.1. Clonar o repositório

Abra o terminal e execute:

```bash
git clone URL_DO_REPOSITORIO
```

Entre na pasta do projeto:

```bash
cd frozen-ice-main
```

### 6.2. Executar o Backend

Acesse a pasta do backend:

```bash
cd frozen-ice-backend
```

Configure as informações de conexão com o banco de dados no arquivo:

```text
src/main/resources/application.properties
```

Verifique as configurações de conexão, usuário, senha e demais propriedades necessárias para execução.

Para iniciar a aplicação utilizando o Maven Wrapper no Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

O backend disponibiliza a API na porta 8080, conforme a configuração padrão do projeto.

Endereço local:

```text
http://localhost:8080
```

### 6.3. Executar o Frontend

Abra outro terminal e acesse a pasta do frontend:

```bash
cd frozen-ice-frontend
```

Instale as dependências:

```bash
npm install
```

Inicie o ambiente de desenvolvimento:

```bash
npm run dev
```

Após a inicialização, o Vite disponibilizará o endereço local da aplicação no terminal, geralmente:

```text
http://localhost:5173
```

Acesse esse endereço pelo navegador para utilizar o sistema.

### 6.4. Executar os testes

Para executar os testes automatizados do backend, acesse a pasta `frozen-ice-backend` e execute:

```powershell
.\mvnw.cmd test
```

O Maven realizará a execução dos testes configurados no projeto e apresentará os resultados no terminal.

## 7. Observações

- O backend deve estar em execução para que o frontend consiga realizar as operações que dependem da API.
- O banco de dados precisa estar configurado e disponível antes da inicialização do backend.
- As configurações de ambiente e credenciais não devem ser compartilhadas publicamente.
- O arquivo `.env.example`, quando utilizado, serve como referência para a configuração das variáveis de ambiente.
- Os arquivos gerados durante a execução, como `node_modules`, `target` e `dist`, não precisam ser enviados ao repositório.

## 8. Considerações Finais

O FROZEN-ICE tem como objetivo oferecer uma solução integrada para o gerenciamento de uma sorveteria, facilitando o controle das operações diárias e centralizando informações importantes para a administração do estabelecimento.

O desenvolvimento do projeto também proporcionou a aplicação prática de conceitos de programação, integração entre frontend e backend, persistência de dados, autenticação, controle de acesso e testes de software.
