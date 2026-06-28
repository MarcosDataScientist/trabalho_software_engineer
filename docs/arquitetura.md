# Arquitetura do Sistema de Biblioteca

## 1. Visão Conceitual da Arquitetura

O sistema de biblioteca é uma aplicação web back-end desenvolvida com **Spring Boot**,
seguindo uma arquitetura **monolítica em camadas** com o padrão **MVC (Model-View-Controller)**.

Toda a lógica da aplicação está contida em um único projeto, organizado em camadas
bem definidas com responsabilidades separadas. A comunicação com o cliente ocorre
via API REST, e a persistência dos dados é feita em um banco de dados relacional
através do Spring Data JPA.

---


Cliente (Frontend / Postman)
        │
        ▼
[ Controller Layer ]
        │
        ▼
[ Service Layer ]
        │
        ▼
[ Repository Layer (DAO) ]
        │
        ▼
[ Banco de Dados Relacional ]



## 2. Descrição dos Elementos da Arquitetura e Dependências

### 2.1 Controller Layer (Camada de Apresentação)
Responsável por receber as requisições HTTP, delegar o processamento
para a camada de serviço e retornar as respostas ao cliente.

**Classes:**
- `AlunoController` — gerencia endpoints de cadastro, atualização,
  busca e exclusão de alunos
- `LivroController` — gerencia endpoints de livros, títulos, áreas e autores
- `EmprestimoController` — gerencia endpoints de empréstimo e devolução de livros

**Dependências:** depende apenas da camada de Service.

---

### 2.2 Service Layer (Camada de Negócio)
Responsável por toda a lógica de negócio da aplicação, validações,
regras e orquestração das operações.

**Classes:**
- `AlunoService` — regras de cadastro e atualização de alunos,
  validação de CPF e matrícula duplicados
- `LivroService` — regras de cadastro de livros, títulos, áreas e autores,
  validação de ISBN duplicado
- `EmprestimoService` — regras de empréstimo (verificação de pendências financeiras,
  disponibilidade do livro) e devolução (cálculo de multa, registro de débito,
  fechamento do empréstimo)
- `MultaProperties` — configuração externa do valor da multa por dia,
  carregada do `application.properties`

**Dependências:** depende da camada de Repository e dos Models.

---

### 2.3 Repository Layer — DAO (Camada de Persistência)
Responsável pelo acesso ao banco de dados. Segue o padrão **DAO (Data Access Object)**,
abstraindo as operações de persistência através de interfaces que estendem `JpaRepository`.

**Interfaces:**
- `AlunoRepository` — operações de persistência do Aluno, incluindo busca
  por CPF e busca com débitos via JPQL
- `LivroRepository` — operações de persistência do Livro, incluindo busca
  de livros disponíveis e busca para empréstimo com JOIN FETCH
- `TituloRepository` — operações de persistência do Título,
  incluindo validação de ISBN
- `AreaRepository` — operações de persistência da Área
- `AutorRepository` — operações de persistência do Autor
- `EmprestimoRepository` — operações de persistência do Empréstimo,
  incluindo busca para devolução e busca de empréstimos ativos por matrícula
- `DebitoRepository` — operações de persistência do Débito

**Dependências:** depende apenas do banco de dados via JPA/Hibernate.

---

### 2.4 Model Layer (Camada de Domínio)
Representa as entidades do domínio da aplicação, mapeadas para tabelas
do banco de dados via JPA.

**Entidades:**
- `Aluno` — representa o aluno, possui matrícula, CPF, nome, endereço
  e relacionamentos com empréstimos, reservas e débitos
- `Livro` — representa um exemplar físico, vinculado a um Título,
  com controle de disponibilidade
- `Titulo` — representa a obra (livro), com ISBN, prazo de empréstimo,
  edição, ano e área
- `Area` — representa a área do conhecimento do título
- `Autor` — representa o autor de um título
- `TituloAutor` — entidade de associação entre Título e Autor
- `Emprestimo` — representa um empréstimo, com datas, multa, atraso
  e lista de itens
- `ItemEmprestimo` — representa cada livro dentro de um empréstimo,
  com data prevista e data de devolução
- `Debito` — representa uma pendência financeira do aluno
- `Reserva` / `ReservaLivro` — representam reservas de livros por alunos

**Dependências:** não depende de nenhuma outra camada.

---

### 2.5 DTOs (Data Transfer Objects)
Objetos utilizados para transferência de dados entre o cliente e a aplicação,
evitando expor diretamente as entidades do domínio.

**Exemplos:**
- `EmprestimoRequest` / `EmprestimoResponse`
- `DevolucaoRequest` / `DevolucaoResponse`
- `LivroRequest` / `LivroResponse`
- `AlunoRequest` / `AlunoResponse`

---

### 2.6 Tratamento de Exceções
- `ResourceNotFoundException` — lançada quando um recurso não é encontrado,
  resulta em HTTP 404
- `BusinessRuleException` — lançada quando uma regra de negócio é violada,
  resulta em HTTP 422

---

## 3. Justificativa da Escolha Arquitetural

### 3.1 Arquitetura Monolítica
O sistema foi desenvolvido como um monolito por ser uma aplicação de **escopo
acadêmico e domínio bem delimitado**, onde a complexidade não justifica
a adoção de microsserviços. As vantagens nesse contexto são:

- **Simplicidade de desenvolvimento e deploy** — toda a aplicação é
  construída, testada e implantada como uma única unidade
- **Menor overhead operacional** — não há necessidade de gerenciar
  múltiplos serviços, comunicação entre eles ou infraestrutura distribuída
- **Facilidade de manutenção** — o código está centralizado,
  facilitando a navegação e o entendimento do sistema como um todo
- **Transações simples** — operações como empréstimo e devolução
  envolvem múltiplas entidades e se beneficiam de transações locais
  gerenciadas pelo `@Transactional` do Spring

### 3.2 Padrão MVC em Camadas
A separação em camadas Controller → Service → Repository segue o padrão
**MVC adaptado para APIs REST**, onde:

- **Controller** assume o papel do *Controller* do MVC,
  recebendo e respondendo requisições
- **Service** concentra a lógica de negócio,
  mantendo os Controllers e Repositories simples e coesos
- **Repository** abstrai o acesso a dados,
  seguindo o padrão **DAO**, permitindo que a camada de negócio
  não conheça detalhes de persistência

As vantagens dessa abordagem são:

- **Separação de responsabilidades** — cada camada tem uma função clara,
  facilitando testes e manutenção
- **Baixo acoplamento** — a troca do banco de dados ou do framework de
  persistência impacta apenas a camada de Repository
- **Alta coesão** — as regras de negócio estão concentradas no Service,
  não espalhadas pelos Controllers ou entidades
- **Testabilidade** — cada camada pode ser testada de forma isolada
  com o uso de mocks
- **Alinhamento com Spring Boot** — o framework foi projetado para
  este padrão, oferecendo suporte nativo via `@RestController`,
  `@Service` e `@Repository`
