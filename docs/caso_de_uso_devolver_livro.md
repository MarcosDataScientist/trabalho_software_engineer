ATOR PRINCIPAL: BIBLIOTECARIA

PRE-CONDICAO: O aluno deve estar cadastrado no sistema / Deve existir um empréstimo ativo associado ao aluno ou ao livro informado / O livro informado deve estar vinculado a um ItemEmprestimo ainda não devolvido/ A bibliotecária deve estar autenticada e autorizada a registrar devoluções.

================================================================================

FLUXO PRINCIPAL:

1 - A bibliotecária informa os dados necessários para registrar a devolução. O sistema pode receber o identificador do empréstimo e a lista de livros devolvidos, ou o identificador do aluno e os livros que estão sendo devolvidos.

2 - O sistema localiza o empréstimo informado atraves das informacoes coletadas na etapa anterior.

3 - O sistema verifica se o empréstimo possui itens passíveis de devolução.

4 - Para cada livro informado, o sistema localiza o respectivo ItemEmprestimo dentro do empréstimo.

5 - O sistema verifica se o item ainda não foi devolvido.

6 - O sistema registra a data de devolução do item com a data atual.

7 - O sistema verifica se houve atraso comparando a data atual com a dataPrevista do item.

8 - Caso não haja atraso, o sistema apenas confirma a devolução do item.

9 - Caso haja atraso, o sistema calcula a multa correspondente e registra um débito para o aluno.

10 - O sistema altera o livro associado ao item para disponível novamente.

11 - Após processar todos os livros informados, o sistema verifica se todos os itens do empréstimo foram devolvidos.

12 - Se todos os itens foram devolvidos, o sistema registra a dataDevolucao do empréstimo com a data atual.

13 - O sistema atualiza o campo atraso do empréstimo caso qualquer item tenha sido devolvido fora do prazo.

14 - O sistema atualiza o campo multa do empréstimo com a soma das multas geradas.

15 - O sistema persiste todas as alterações.

16 - O sistema retorna a confirmação da devolução contendo os livros devolvidos, a data de devolução, a existência ou não de atraso e o valor total de multa gerado.

FLUXOS ALTERNATIVOS:

2.a - Se o sistema não encontrar o empréstimo informado, a operação deve ser encerrada e o usuario informado ("Empréstimo não localizado, verifique as informações fornecidas!").

3.a - Se o empréstimo não possuir itens passíveis de devolução aborta a devolução informando o usuário ("A devolucão não é possível, verifique as informações fornecidas!")

4.a - Se algum livro informado não estiver associado ao empréstimo informado, a operação deve ser encerrada.
O usuário deve ser informado ("Existem livros nao associados ao emprestimo. Verifique a lista de livros!")

5.a - Se algum item do emprestimo ja possuir data de devolucao, a operacao deve ser encerrada. 
("Existem livros já devolvidos anteriormente, favor verificar as informações.")

7.a - Caso a data devolucao seja posterior a data prevista, o sistema calcula a multa proporcional a quantidade de dias em atraso. ("Devolução atrasada!")

9.a - Caso nao seja possível registrar o débito do aluno, a devolução deve ser cancelada ("Erro ao registrar débito, devolução cancelada...")

================================================================================
Pós-condição:
Os ItemEmprestimo devolvidos devem possuir dataDevolucao preenchida/ Os livros devolvidos devem ser marcados como disponíveis/
Caso todos os itens do empréstimo tenham sido devolvidos, o Emprestimo deve possuir dataDevolucao preenchida/ Caso haja atraso, o empréstimo deve ser marcado com atraso = true / Caso haja multa, o valor total deve ser registrado no campo multa do empréstimo / Caso haja multa, deve ser criado um registro em Debito associado ao aluno /A operação deve ser transacional: ou todas as alterações são persistidas, ou nenhuma alteração é aplicada.

REGRA DE MULTA:
multaPorLivro = diasAtraso × biblioteca.multa.valor-por-dia (configurado em application.properties)
totalMulta = soma das multas dos livros devolvidos com atraso → registrado em Emprestimo.multa e em Debito.valor
