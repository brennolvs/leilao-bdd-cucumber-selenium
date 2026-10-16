# Leilão — Automação de Testes com BDD, Cucumber e Selenium

Projeto de estudo de automação de testes em Java aplicado a um sistema web de leilões (Spring Boot). O foco do repositório é a **suíte de testes**: cobre as camadas de unidade, integração, ponta a ponta (E2E) com Selenium e aceitação com BDD/Cucumber escrito em português.

> A aplicação web sob teste foi usada como base de estudos durante um curso de testes automatizados. Os testes, a organização da suíte e as melhorias feitas em cima dela são o objeto deste projeto.

## Sobre a aplicação sob teste

Um sistema simples de leilões em que usuários autenticados podem:

- fazer login;
- cadastrar e editar leilões (nome, valor inicial e data de abertura);
- propor lances em leilões de outros usuários.

### Regras de negócio cobertas pelos testes

| Regra | Onde é testada |
|---|---|
| Lance deve ter valor maior que zero | `LanceTest`, `propondo_lances.feature` |
| Lance deve ser maior que o último lance dado | `LeilaoTest` |
| Um usuário não pode dar dois lances seguidos | `LeilaoTest`, `LanceE2ETest`, `propondo_lances.feature` |
| Não são aceitos dois lances com o mesmo valor | `LeilaoTest`, `LanceE2ETest`, `LanceServiceTest` |
| Um usuário pode dar no máximo 5 lances por leilão | `LeilaoTest` |
| Apenas usuários cadastrados conseguem logar | `login.feature` |
| Usuário logado consegue cadastrar um leilão | `leilao.feature`, `LeiloesE2ETest` |

## Tecnologias

| Categoria | Ferramentas |
|---|---|
| Linguagem / build | Java 8, Maven |
| Aplicação | Spring Boot 2.3, Spring MVC, Spring Security, Spring Data JPA, Thymeleaf |
| Testes | JUnit 5 (com JUnit 4 para o runner do Cucumber), Hamcrest, Mockito, MockMvc |
| BDD | Cucumber 6 (Gherkin em português) |
| E2E | Selenium WebDriver (Chrome, Firefox e HtmlUnit) |
| Banco de dados | H2 em memória (perfil `test`) e MariaDB (perfil `prod`) |
| Infra | Docker Compose (MariaDB + Adminer) |

## Estratégia de testes

A suíte segue a pirâmide de testes, das camadas mais rápidas e isoladas para as mais lentas e completas:

```
            ▲   Aceitação (BDD / Cucumber)
           ▲▲▲  E2E (Selenium + Page Objects)
         ▲▲▲▲▲  Integração (Spring Boot Test, MockMvc, Mockito, DataJpaTest)
       ▲▲▲▲▲▲▲  Unidade (JUnit 5)
```

**Unidade** (`model/`): validam as regras de lance diretamente nas entidades `Leilao` e `Lance`, sem subir o Spring. O `LeilaoTest` também usa `@Nested` e `@DisplayName` para organizar os cenários no estilo Dado/Quando/Então.

**Integração** (`integration/`):
- `LeiloesControllerTest`: sobe o contexto e usa **MockMvc** para validar que a página de leilões responde corretamente.
- `LanceServiceTest`: testa o serviço de lances isolando os repositórios com **Mockito** (`@MockBean`).
- `UsuarioRepositoryTest`: testa a consulta do repositório com **@DataJpaTest** sobre o H2.

**E2E** (`e2e/`): testes com **Selenium WebDriver** que navegam pela aplicação real rodando em `localhost:8080`. Usam o padrão **Page Object** (`LoginPage`, `LeiloesPage`, `NovoLeilaoPage`, `AlterarLeilaoPage`, `DetalhesDoLeilaoPage`) e uma `BrowserFactory` que escolhe o navegador por parâmetro. Antes de cada teste, a massa de dados é recriada pelo endpoint `/db/seed`, disponível apenas no perfil `test`.

**Aceitação / BDD** (`acceptance/` + `resources/features/`): cenários escritos em **Gherkin em português**, com step definitions que reaproveitam os Page Objects. Exploram recursos do Cucumber como `Contexto`, `Esquema do Cenário` com `Exemplos`, parâmetros tipados (`{double}`, `{string}`), `DataTable` e tags.

## Estrutura do projeto

```
src
├── main/java/br/com/leilao        # aplicação sob teste
│   ├── controller/                # inclui DBController (seed de dados para testes)
│   ├── dto/
│   ├── model/                     # Leilao, Lance, Usuario (regras de negócio)
│   ├── repositories/
│   ├── security/
│   └── service/
└── test
    ├── java/br/com/leilao
    │   ├── model/                 # testes de unidade
    │   ├── integration/           # testes de integração
    │   │   ├── controller/
    │   │   ├── repository/
    │   │   └── service/
    │   ├── e2e/
    │   │   ├── pages/             # Page Objects + BrowserFactory
    │   │   └── selenium/          # testes E2E
    │   └── acceptance/
    │       ├── LeilaoCucumberRunner.java
    │       └── steps/             # step definitions
    └── resources/features/        # cenários .feature (Gherkin pt-BR)
        ├── leilao.feature
        ├── login.feature
        └── propondo_lances.feature
```

## Como executar

### Pré-requisitos

- JDK 8 ou superior
- Maven 3.6+
- Google Chrome e/ou Firefox
- [ChromeDriver](https://googlechromelabs.github.io/chrome-for-testing/) e/ou [GeckoDriver](https://github.com/mozilla/geckodriver/releases) compatíveis com a versão do navegador instalado

Os drivers não são versionados. Baixe-os e coloque na pasta `drivers/` com os nomes `chromedriver.exe` e `geckodriver.exe` (Windows), que é o caminho esperado pela `BrowserFactory`.

### 1. Subir a aplicação

O perfil padrão é `test`, que usa H2 em memória, então não é preciso banco externo:

```bash
mvn spring-boot:run
```

A aplicação fica disponível em `http://localhost:8080`.

<details>
<summary>Opcional: rodar com MariaDB via Docker (perfil prod)</summary>

```bash
docker-compose up -d
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

O Adminer fica disponível em `http://localhost:8081`. Para derrubar os serviços: `docker-compose down`.
</details>

### 2. Rodar os testes

```bash
# testes de unidade
mvn test

# testes de integração
mvn integration-test
```

Os testes **E2E** e os cenários **Cucumber** precisam da aplicação rodando (passo 1). Execute-os pela IDE ou indicando a classe:

```bash
# cenários BDD
mvn test -Dtest=LeilaoCucumberRunner

# escolher o navegador (padrão: chrome)
mvn test -Dtest=LeilaoCucumberRunner -Dbrowser=firefox

# filtrar cenários por tag
mvn test -Dtest=LeilaoCucumberRunner -Dcucumber.filter.tags="@leilao"
```

Valores aceitos em `-Dbrowser`: `chrome`, `firefox` ou qualquer outro valor para usar o **HtmlUnit** (headless, sem abrir navegador).

### Usuários da massa de testes

O endpoint `/db/seed` cria os usuários abaixo, todos com a senha `pass`:

| Usuário | Perfil |
|---|---|
| fulano | USER |
| beltrano | USER |
| cigano | ADMIN |

## Aprendizados

- Organização de uma suíte de testes em camadas, seguindo a pirâmide de testes.
- Escrita de cenários BDD em português e mapeamento para step definitions.
- Recursos do Cucumber: `Contexto`, `Esquema do Cenário`, `DataTable`, parâmetros tipados e tags.
- Padrão Page Object para reduzir duplicação e isolar seletores.
- Esperas explícitas (`WebDriverWait`) e implícitas no Selenium.
- Isolamento de dependências com Mockito e testes de persistência com `@DataJpaTest`.
- Uso de perfis do Spring (`test` / `prod`) para separar ambientes e massa de dados.

## Autor

**Brenno** — QA Analyst
[LinkedIn](https://www.linkedin.com/in/brennolvs)
