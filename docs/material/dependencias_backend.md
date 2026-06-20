# Dependências do Backend (Spring Boot + Java 25)

Este documento descreve as dependências selecionadas no **Spring Initializr** para a implementação do backend do projeto de Engenharia de Software.

---

## 1. Configurações Globais do Projeto
*   **Linguagem:** Java
*   **Versão do Java:** 25 (LTS)
*   **Gerenciador de Dependências:** Gradle - Groovy
*   **Empacotamento:** Jar

---

## 2. Dependências Selecionadas e Justificativas

Abaixo estão listadas as dependências escolhidas e qual o papel de cada uma no ecossistema da nossa aplicação (Monolito MVC com Banco de Dados PostgreSQL).

### 🏛️ Spring Web
*   **O que é:** Biblioteca base para a construção de aplicações web, incluindo suporte a MVC, REST e servidor Tomcat embutido.
*   **Por que usar:** Responsável por gerenciar as requisições HTTP do usuário, mapear as rotas para as páginas HTML (usando controladores `@Controller`) e lidar com dados enviados em formulários.

### 🎨 Thymeleaf (Spring Boot Starter Thymeleaf)
*   **O que é:** Um motor de templates (*template engine*) Java para renderização de páginas HTML5 no lado do servidor.
*   **Por que usar:** Serve como a camada de visualização (View) da nossa arquitetura MVC. Permite ligar diretamente as classes de negócio do backend e exibir os dados no HTML de forma dinâmica (como tabelas de alunos, empréstimos, etc.) sem a necessidade de um frontend desacoplado (Next.js).

### 🗄️ Spring Data JPA
*   **O que é:** Framework que facilita a persistência de dados em bancos de dados relacionais usando a API de Persistência do Java (JPA) e Hibernate.
*   **Por que usar:** Em vez de escrever códigos SQL complexos e manuais (como `INSERT INTO` ou `SELECT * FROM`), o *Spring Data JPA* permite que mapeemos as tabelas como objetos Java comuns (Entidades) e criemos repositórios com consultas automáticas, facilitando as operações de CRUD.

### 🐘 PostgreSQL Driver
*   **O que é:** O driver de banco de dados (JDBC) oficial para conexão com o PostgreSQL.
*   **Por que usar:** Como o banco de dados especificado para este projeto é o PostgreSQL, a aplicação Java precisa deste driver instalado para conseguir se conectar, autenticar e trafegar dados com o serviço de banco de dados rodando em nosso contêiner Docker.

### 🏷️ Lombok
*   **O que é:** Biblioteca focada em produtividade que atua em tempo de compilação inserindo código de forma automática por meio de anotações.
*   **Por que usar:** Evita que tenhamos que escrever e manter manualmente métodos repetitivos como Getters, Setters, Construtores, `equals()`, `hashCode()` e `toString()`. Por exemplo, basta usar a anotação `@Data` no topo de uma classe e todos os getters e setters serão criados por debaixo dos panos.

### 🛡️ Validation (Spring Boot Starter Validation)
*   **O que é:** Framework de validação baseado na especificação Jakarta Bean Validation (Hibernate Validator).
*   **Por que usar:** Permite aplicar regras de validação diretamente nos modelos ou DTOs que recebemos do frontend (ex: garantir que o RA do aluno não venha nulo ou vazio, ou que uma data de devolução seja no futuro). Se os dados forem inválidos, a API rejeita a requisição imediatamente antes de persistir no banco.

### 🛠️ Spring Boot DevTools
*   **O que é:** Conjunto de ferramentas utilitárias para desenvolvimento local.
*   **Por que usar:** Oferece *LiveReload* e reinicialização rápida do servidor. Sempre que você alterar e salvar uma classe Java, a aplicação atualiza o servidor em execução em segundos, sem que você precise parar e iniciar o projeto manualmente.


