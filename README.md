# 📚 Sistema de Biblioteca - Engenharia de Software

Este repositório contém a entrega final da disciplina de **Engenharia de Software** do Centro de Ciências Exatas (CCE) da **Universidade Estadual de Londrina (UEL)**, curso de Ciência de Dados e Inteligência Artificial.

O objetivo do projeto é desenvolver um **Sistema de Biblioteca** funcional, aplicando conceitos de modelagem arquitetural (MVC), padrões de projeto (DAO), testes unitários e práticas de cultura **DevOps** e **Desenvolvimento Ágil** (Scrum/Kanban e CI/CD).

## 👥 Integrantes do Grupo

*   Guilherme Terziotti
*   Lucas Silva
*   Marcos Beregula
*   Michel Iago

---

## 🛠️ Tecnologias Utilizadas

*   **Linguagem:** Java 25 (LTS)
*   **Framework:** Spring Boot (com Spring Web, Spring Data JPA e Validation)
*   **Visualização (View):** Thymeleaf (Monolito MVC renderizado no servidor)
*   **Banco de Dados:** PostgreSQL 15 (Relacional)
*   **Orquestração/Ambiente:** Docker e Docker Compose

---

## 🏫 Requisitos e Funcionalidades do Projeto

### 1. Arquitetura do Sistema (Padrão MVC em Camadas)
A arquitetura do sistema é documentada separadamente na pasta `/docs` contemplando:
*   **Visão Conceitual da Arquitetura:** Estrutura de alto nível do sistema.
*   **Descrição dos Elementos e Dependências:** Detalhamento de componentes, módulos e interações.
*   **Padrões Arquiteturais:** Justificativa da escolha do padrão monolítico em camadas (MVC).

### 2. Funcionalidades Essenciais
*   **Cadastro de Livros e Alunos:** Implementação de telas (GUI via Thymeleaf) para cadastro completo de livros e alunos.
*   **Caso de Uso: Emprestar Livro:**
    *   Finalização das regras do caso de uso com base na especificação do livro *Engenharia de Software Orientado a Objetos* (Exercícios 2 a 6, página 197).
    *   Diagramas de Classe e Sequência atualizados (inserindo camada de persistência).
    *   Persistência de dados utilizando o **Padrão DAO (Data Access Object)** no banco PostgreSQL.
*   **Caso de Uso: Devolver Livro:**
    *   Descrição completa do caso de uso.
    *   Diagramas de Caso de Uso, Classes e Sequência específicos para a devolução.
    *   Implementação prática integrada ao banco de dados com regras de multas/prazos calculados.

### 3. Testes Automatizados
*   Implementação de casos de testes unitários para as regras de negócio de **Emprestar** e **Devolver** livros usando JUnit.
*   Automação dos testes para execução automatizada no pipeline.

---

## ♾️ Processo DevOps e Ágil (Requisitos Obrigatórios)

*   **Planejamento Ágil:** Backlog do produto e gerenciamento de Sprints definidos no **GitHub Projects (Quadro Kanban)** com as colunas *To Do*, *In Progress*, *Review* e *Done*.
*   **Gestão de Tarefas (Issues):** Divisão de funcionalidades em Issues do GitHub, categorizadas com *Labels* apropriadas e associadas aos responsáveis.
*   **Estratégia de Branches:** Desenvolvimento feito exclusivamente em branches de funcionalidade (ex: `feature/cadastro-livro`).
*   **Revisão de Código:** Submissão de Pull Requests (PRs) com obrigatoriedade de no mínimo 2 revisões (*Code Review*) por membros da equipe antes do merge na branch principal.
*   **Integração Contínua (Pipeline CI):** Configuração de workflow do **GitHub Actions** para validar automaticamente a compilação do código e executar os testes unitários a cada Pull Request aberto.
*   **Release:** Publicação de uma versão oficial do sistema (Release) no GitHub ao final do projeto, vinculada à conclusão de todas as tarefas no Project.

---

## 🚀 Instruções de Execução e Setup

O ambiente de desenvolvimento está totalmente conteinerizado utilizando o Docker Compose, eliminando a necessidade de instalar o banco de dados e o JDK localmente na sua máquina para rodar a aplicação.

### Pré-requisitos
*   **Docker** e **Docker Compose** instalados na máquina.

### Executando o Projeto

1.  Clone este repositório no seu ambiente de desenvolvimento.
2.  No diretório raiz do projeto (onde se encontra o arquivo `docker-compose.yml`), abra o terminal e execute:
    ```bash
    docker compose up --build
    ```
3.  Aguarde até que o contêiner do PostgreSQL e do Backend Java 25 terminem de inicializar.
4.  Acesse a aplicação em seu navegador no endereço:
    ```url
    http://localhost:8080
    ```

*Nota: Qualquer modificação feita no código-fonte em tempo de desenvolvimento na pasta `backend/` acionará o Hot Reload do Spring DevTools automaticamente.*

---

## 📂 Estrutura do Diretório

```bash
├── backend/                  # Código-fonte do Monolito Spring Boot
│   ├── src/                  # Código Java e templates HTML (Thymeleaf)
│   ├── Dockerfile            # Configuração do contêiner do backend
│   └── build.gradle          # Arquivo de dependências Gradle
├── docs/                     # Documentação de arquitetura e processos do projeto
├── docker-compose.yml        # Configuração unificada do ambiente Docker (PostgreSQL + App)
└── README.md                 # Instruções e resumo do projeto
```

> [!NOTE]
> Ao final do projeto, será gerado o PDF com o relatório completo de conclusões em docs/relatorio_final.pdf.
