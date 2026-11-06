# CRUD de Produtos — MVC com Spring Boot

Aplicação web simples de cadastro de produtos usando o padrão MVC:

- **Model** — `Produto`
- **Repository** — armazenamento em memória (`ConcurrentHashMap`)
- **Service** — regras de negócio
- **Controller** — rotas HTTP
- **View** — templates Thymeleaf + CSS

## Funcionalidades

- Listar produtos
- Cadastrar produto
- Editar produto
- Excluir produto
- Validação de campos (nome, preço e quantidade)
- Mensagens de sucesso após cada operação
- Preço aceita vírgula ou ponto (`19,90`, `19.90`, `1.234,56`)

## Pré-requisitos

- Java 17 ou superior
- Maven 3.8+ (opcional, o projeto inclui o Maven Wrapper)

## Como executar

Com o Maven Wrapper (recomendado):

```bash
./mvnw spring-boot:run
```

No Windows:

```bash
mvnw.cmd spring-boot:run
```

Ou, se já tiver o Maven instalado:

```bash
mvn spring-boot:run
```

Depois acesse: <http://localhost:8080>

A página inicial redireciona para `/produtos`.

## Como testar

```bash
./mvnw test
```

## Estrutura do projeto

```
src/main/java/com/example/produtos/
├── ProdutosApplication.java
├── controller/
│   ├── HomeController.java
│   └── ProdutoController.java
├── model/
│   └── Produto.java
├── repository/
│   └── ProdutoRepository.java
└── service/
    └── ProdutoService.java

src/main/resources/
├── application.properties
├── messages.properties
├── static/css/style.css
└── templates/produtos/
    ├── formulario.html
    └── lista.html
```

## Rotas

| Método | Rota | Ação |
|--------|------|------|
| GET | `/` | Redireciona para `/produtos` |
| GET | `/produtos` | Lista os produtos |
| GET | `/produtos/novo` | Formulário de cadastro |
| POST | `/produtos` | Cria um produto |
| GET | `/produtos/{id}/editar` | Formulário de edição |
| POST | `/produtos/{id}` | Atualiza um produto |
| POST | `/produtos/{id}/excluir` | Exclui um produto |

## Observação sobre persistência

Os dados ficam **em memória**. Ao encerrar a aplicação, eles são perdidos e os
três produtos de exemplo são recriados na próxima inicialização.

Para persistir de verdade, basta trocar o `ProdutoRepository` por uma
implementação com JPA + banco (H2, PostgreSQL etc.) mantendo a mesma interface
usada pelo `ProdutoService`.
