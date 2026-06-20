# Intuito
Arquivo direcionado a conversões utilizadas para commits e abertura de branchs.

Contemplando o seguinte requisito do trabalho:
"Branches:
Utilizar branches para o desenvolvimento de novas funcionalidades
(ex: feature/cadastro-livro, feature/emprestar-livro).
Commits:
Realizar commits frequentes e descritivos, seguindo boas práticas de
mensagens de commit."

Nota:
Também teremos a utilização de PR (Pull Request) para mesclagem de branchs, para atender o seguinte requisito:
"Pull Requests: 
Realizar a revisão de código via Pull Requests (PRs) no GitHub."

Também irei realizar a configuração do Github Actions para contemplar esta parte do trabalho:
"Automação: 
Configurar pelo menos uma automação com GitHub Actions. Esta automação deve ser executada automaticamente quando um Pull Request for aberto. Exemplos de automação incluem:
- Executar build ou testes automaticamente.
- Enviar notificação (ex: log de execução).
- Validar Pull Requests (ex: verificar se o código compila, rodar testes
unitários).
Pipeline CI:
O objetivo é criar um pipeline simples de Integração Contínua(CI)."


Pontos importantes:
"Criação de Release: 
- Criar uma versão do sistema (Release) no GitHub ao final do projeto.
- Descrição da Release: Descrever claramente o que foi entregue nesta versão.
- Conclusão de Tarefas: Garantir que todas as tarefas relacionadas à Release estejam finalizadas no GitHub Project."

# Commits
Adotamos o padrão de **Conventional Commits** para manter o histórico de commits organizado e legível. Cada commit deve seguir a estrutura:
`<tipo>(<escopo opcional>): <descrição curta>`

### Tipos de Commit:
* **`feat`**: Introdução de uma nova funcionalidade (ex: `feat: adicionar endpoint de cadastro de livro`).
* **`fix`**: Correção de um bug (ex: `fix: corrigir validação de data de devolução`).
* **`docs`**: Alterações exclusivas na documentação (ex: `docs: atualizar guia de commits no readme`).
* **`style`**: Mudanças de estilo que não afetam a lógica do código (espaços em branco, formatação, etc.) (ex: `style: formatar código do controller`).
* **`refactor`**: Refatoração de código que não corrige bug nem adiciona funcionalidade (ex: `refactor: simplificar consulta de livros`).
* **`perf`**: Alteração de código com foco em melhoria de performance (ex: `perf: otimizar indexação do banco`).
* **`test`**: Criação ou ajuste de testes automatizados (ex: `test: adicionar testes unitários de empréstimo`).
* **`chore`**: Atualizações de tarefas de build, dependências ou ferramentas de desenvolvimento (ex: `chore: atualizar dependências do Gradle`).
* **`ci`**: Alterações em arquivos de configuração e scripts de CI (ex: `ci: adicionar workflow do GitHub Actions`).

### Regras de ouro para Commits:
1. Use verbos no infinitivo ou no imperativo na descrição (ex: `adicionar` ou `adicione`, evite `adicionado` ou `adicionando`).
2. Mantenha a primeira letra da descrição em minúsculo.
3. Não coloque ponto final na mensagem do commit.
4. Mantenha os commits atômicos (um commit por alteração conceitual).

# Branches
Utilizamos um fluxo de desenvolvimento baseado em ramificações curtas (*feature branches*). Nenhuma alteração deve ser feita diretamente na branch principal.

### Padrão de Nomenclatura:
* **`main`**: Branch de produção. Deve estar sempre estável e pronta para deploy.
* **`feature/<nome-funcionalidade>`**: Desenvolvimento de novas funcionalidades.
  * *Exemplos:* `feature/cadastro-livro`, `feature/emprestar-livro`.
* **`bugfix/<nome-do-bug>`**: Correção de problemas em ambiente de teste ou homologação.
  * *Exemplo:* `bugfix/corrigir-calculo-multa`.
* **`hotfix/<nome-da-correcao>`**: Correção crítica e urgente realizada diretamente a partir de produção.
  * *Exemplo:* `hotfix/vulnerabilidade-seguranca`.
* **`chore/<tarefa-configuracao>`**: Tarefas de manutenção ou configuração.
  * *Exemplo:* `chore/configurar-actions`.

# PR (Pull Requests)
Toda integração de código na branch `main` deve ser realizada por meio de Pull Requests.

### Boas práticas:
* **Descrição Clara**: O PR deve conter um resumo claro do que foi implementado e o motivo.
* **Validação Automática**: O pipeline de CI deve executar com sucesso antes do merge ser permitido.
* **Code Review**: O código deve ser revisado e aprovado por pelo menos outro membro do grupo antes do merge.
* **Vínculo com Tarefas**: Vincular o PR às issues/cards correspondentes no GitHub Projects para fechar automaticamente após o merge (ex: `Closes #12`).

# Releases
Ao término de cada etapa significativa ou no fim do projeto, criaremos uma **Release** no GitHub.

* **Versionamento Semântico**: Seguiremos o padrão `vMAJOR.MINOR.PATCH` (ex: `v1.0.0`).
* **Notas de Release (Changelog)**: Descrever claramente todas as entregas agrupadas por categoria (Novas Features, Correções, etc.).
* **Fechamento de Tarefas**: Validar que todos os cartões associados à release estejam como "Done" no GitHub Project.

# Pipeline CI
O pipeline de Integração Contínua (CI) será configurado em `.github/workflows/ci.yml`.
* **Gatilho**: Disparado automaticamente a cada abertura ou atualização de Pull Request para a branch `main`.
* **Ações**:
  - Configuração do JDK 21 (ou correspondente).
  - Execução do build do projeto backend (`./gradlew build`).
  - Execução dos testes unitários e de integração (`./gradlew test`).
  - O merge só será liberado caso todas as etapas passem com sucesso.

# Pipeline CD
No intuito de realizar um entrega próxima a um ambiente real também iremos realizar o pipeline para deploy da aplicação.
* A automação de CD estará localizada em `.github/workflows/deploy.yml` e será disparada após o merge na `main`.
* Realizará o build e publicação da imagem Docker ou deploy direto no provedor de Cloud selecionado.