// Função para exibir mensagem em modal customizado
function mostrarMensagem(titulo, mensagem, mostrarCancelar = false) {
    return new Promise((resolve) => {
        const modal = document.getElementById('custom-modal');
        const titleEl = document.getElementById('modal-title');
        const messageEl = document.getElementById('modal-message');
        const btnConfirm = document.getElementById('modal-btn-confirm');
        const btnCancel = document.getElementById('modal-btn-cancel');

        if (!modal) {
            // Fallback para os diálogos nativos do navegador caso o modal não esteja carregado
            if (mostrarCancelar) {
                resolve(confirm(mensagem));
            } else {
                alert(mensagem);
                resolve(true);
            }
            return;
        }

        titleEl.innerText = titulo;
        messageEl.innerText = mensagem;

        // Mostrar ou ocultar botão Cancelar
        btnCancel.style.display = mostrarCancelar ? 'block' : 'none';

        // Event Listeners
        const handleConfirm = () => {
            cleanup();
            resolve(true);
        };

        const handleCancel = () => {
            cleanup();
            resolve(false);
        };

        const cleanup = () => {
            modal.classList.remove('show');
            btnConfirm.removeEventListener('click', handleConfirm);
            btnCancel.removeEventListener('click', handleCancel);
            setTimeout(() => {
                modal.style.display = 'none';
            }, 300);
        };

        btnConfirm.addEventListener('click', handleConfirm);
        btnCancel.addEventListener('click', handleCancel);

        // Exibir o modal com efeito de fade
        modal.style.display = 'flex';
        modal.offsetHeight; // Força reflow para aplicar transição
        modal.classList.add('show');
    });
}

// dropdown and user menu toggling
function toggleDropdown(event) {
    event.stopPropagation();
    const dropdown2 = document.getElementById('user-dropdown-menu');
    if (dropdown2) dropdown2.classList.remove('show');
    const arrow = document.getElementById('user-arrow');
    if (arrow) arrow.style.transform = 'rotate(0deg)';

    const dropdown = document.getElementById('dropdown-menu');
    if (dropdown) dropdown.classList.toggle('show');
}

function toggleUserDropdown(event) {
    event.stopPropagation();
    const dropdown2 = document.getElementById('dropdown-menu');
    if (dropdown2) dropdown2.classList.remove('show');

    const dropdown = document.getElementById('user-dropdown-menu');
    if (dropdown) dropdown.classList.toggle('show');

    const arrow = document.getElementById('user-arrow');
    if (arrow) {
        if (dropdown.classList.contains('show')) {
            arrow.style.transform = 'rotate(180deg)';
        } else {
            arrow.style.transform = 'rotate(0deg)';
        }
    }
}

window.onclick = function (event) {
    const dropdowns = document.getElementsByClassName("dropdown-content");
    for (let i = 0; i < dropdowns.length; i++) {
        let openDropdown = dropdowns[i];
        if (openDropdown.classList.contains('show')) {
            openDropdown.classList.remove('show');
        }
    }
    const arrow = document.getElementById('user-arrow');
    if (arrow) arrow.style.transform = 'rotate(0deg)';
}

// Student registration API call
async function salvarAluno() {
    const matricula = document.getElementById('matricula').value.trim();
    const nome = document.getElementById('nome').value.trim();
    const cpf = document.getElementById('cpf').value.trim();
    const endereco = document.getElementById('endereco').value.trim();

    if (!matricula || !nome || !cpf) {
        await mostrarMensagem("Aviso", "Por favor, preencha todos os campos obrigatórios.");
        return;
    }

    if (matricula === "10") {
        await mostrarMensagem("Erro de Matrícula", "Erro: RA '10' é um RA de teste que sempre possui débitos/bloqueios. Por favor, utilize outro.");
        return;
    }

    try {
        const response = await fetch('/api/alunos', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                matricula: parseInt(matricula, 10),
                nome: nome,
                cpf: cpf,
                endereco: endereco
            })
        });

        if (response.ok) {
            await mostrarMensagem("Sucesso", `Aluno Cadastrado com Sucesso!\n\nNome: ${nome}\nRA: ${matricula}\n\nO registro foi salvo no banco de dados.`);
            document.getElementById('form-aluno').reset();
            location.href = '/';
        } else {
            const errData = await response.json();
            await mostrarMensagem("Erro", `Erro ao cadastrar aluno: ${errData.message || response.statusText}`);
        }
    } catch (error) {
        console.error(error);
        await mostrarMensagem("Erro de Conexão", "Erro de conexão ao tentar cadastrar o aluno.");
    }
}

// Book registration (API call)
async function salvarLivro() {
    const codigo = document.getElementById('codigo').value;
    const titulo = document.getElementById('titulo').value.trim();
    const autorNomeCompleto = document.getElementById('autor').value.trim();
    const prazo = document.getElementById('prazo').value;
    
    if (!codigo || !titulo || !autorNomeCompleto || !prazo) {
        await mostrarMensagem("Aviso", "Preencha todos os campos.");
        return;
    }

    try {
        let areaId = 1;
        const areasRes = await fetch('/api/livros/catalogo/areas');
        if (areasRes.ok) {
            const areas = await areasRes.json();
            if (areas.length > 0) {
                areaId = areas[0].id;
            } else {
                const newAreaRes = await fetch('/api/livros/catalogo/areas', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ nome: "Geral", descricao: "Área geral do catálogo" })
                });
                if (newAreaRes.ok) {
                    const newArea = await newAreaRes.json();
                    areaId = newArea.id;
                }
            }
        }

        let autorId = 1;
        const nomes = autorNomeCompleto.split(' ');
        const nome = nomes[0];
        const sobrenome = nomes.length > 1 ? nomes.slice(1).join(' ') : "";
        
        const autorRes = await fetch('/api/livros/catalogo/autores', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ nome: nome, sobrenome: sobrenome, titulacao: "Autor(a)" })
        });
        if (autorRes.ok) {
            const autor = await autorRes.json();
            autorId = autor.id;
        }

        const livroRes = await fetch('/api/livros', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                titulo: titulo,
                prazo: parseInt(prazo, 10),
                isbn: codigo,
                areaId: areaId,
                autorIds: [autorId],
                disponivel: true,
                exemplarBiblioteca: true
            })
        });

        if (livroRes.ok) {
            await mostrarMensagem("Sucesso", `Livro Cadastrado com Sucesso!\n\nTítulo: ${titulo}\nAutor: ${autorNomeCompleto}\nCódigo: ${codigo}\nPrazo de devolução: ${prazo} dias\n\nO item foi adicionado ao catálogo e salvo no banco de dados.`);
            document.getElementById('form-livro').reset();
            location.href = '/dashboard';
        } else {
            const errData = await livroRes.json();
            await mostrarMensagem("Erro", `Erro ao cadastrar livro: ${errData.message || livroRes.statusText}`);
        }
    } catch (error) {
        console.error(error);
        await mostrarMensagem("Erro de Conexão", "Erro de conexão ao tentar cadastrar o livro no banco de dados.");
    }
}

// Book lending API call
async function emprestarLivro(livroId, titulo, matricula) {
    try {
        const response = await fetch('/api/emprestimos', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                matricula: matricula,
                livrosIds: [livroId]
            })
        });

        if (response.ok) {
            const card = document.getElementById(`livro-${livroId}`);
            if (card) {
                card.style.opacity = '0';
                card.style.transform = 'translateY(10px)';
                card.style.transition = 'all 0.5s ease';

                setTimeout(async () => {
                    card.remove();
                    await mostrarMensagem("Empréstimo Realizado", `Livro "${titulo}" emprestado com sucesso!`);
                    
                    const lista = document.getElementById('lista-emprestimo');
                    const livrosRestantes = lista ? lista.getElementsByClassName('livro') : [];
                    if (livrosRestantes.length === 0) {
                        const msgVazia = document.getElementById('mensagem-vazia');
                        if (msgVazia) msgVazia.style.display = 'block';
                    }
                }, 500);
            } else {
                await mostrarMensagem("Empréstimo Realizado", `Livro "${titulo}" emprestado com sucesso!`);
            }
        } else {
            const errData = await response.json();
            await mostrarMensagem("Erro", `Erro ao emprestar livro: ${errData.message || response.statusText}`);
        }
    } catch (error) {
        console.error(error);
        await mostrarMensagem("Erro de Conexão", "Erro de conexão ao tentar emprestar o livro.");
    }
}

// Book return API call
async function devolverLivro(emprestimoId, matricula, livroId) {
    const confirmado = await mostrarMensagem("Confirmar Devolução", "Deseja realmente devolver este livro?", true);
    if (!confirmado) {
        return;
    }

    try {
        const response = await fetch('/api/emprestimos/devolver', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                emprestimoId: emprestimoId,
                matricula: matricula,
                livrosIds: [livroId]
            })
        });

        if (response.ok) {
            const card = document.getElementById(`livro-${emprestimoId}`);
            if (card) {
                card.style.opacity = '0';
                card.style.transform = 'translateY(10px)';
                card.style.transition = 'all 0.5s ease';

                setTimeout(async () => {
                    card.remove();
                    await mostrarMensagem("Devolução Concluída", "Livro devolvido com sucesso!");
                    
                    const lista = document.getElementById('lista-livros');
                    const livrosRestantes = lista ? lista.getElementsByClassName('livro') : [];
                    if (livrosRestantes.length === 0) {
                        const msgVazia = document.getElementById('mensagem-vazia');
                        if (msgVazia) msgVazia.style.display = 'block';
                    }
                }, 500);
            } else {
                await mostrarMensagem("Devolução Concluída", "Livro devolvido com sucesso!");
            }
        } else {
            const errData = await response.json();
            await mostrarMensagem("Erro", `Erro ao devolver livro: ${errData.message || response.statusText}`);
        }
    } catch (error) {
        console.error(error);
        await mostrarMensagem("Erro de Conexão", "Erro de conexão ao tentar devolver o livro.");
    }
}

// Fechar outros dropdowns ao abrir um novo (header nav)
function fecharOutrosDropdowns(excecaoId) {
    const dropdowns = document.getElementsByClassName("dropdown-content");
    for (let i = 0; i < dropdowns.length; i++) {
        if (dropdowns[i].id !== excecaoId) {
            dropdowns[i].classList.remove('show');
        }
    }
}

// Realizar emprestimo (formulario novo)
async function realizarEmprestimo() {
    const alunoSelect = document.getElementById('aluno-select');
    const livroSelect = document.getElementById('livro-select');
    
    if (!alunoSelect.value || !livroSelect.value) {
        await mostrarMensagem("Aviso", "Selecione o aluno e o livro.");
        return;
    }

    const matricula = alunoSelect.value;
    const livroId = livroSelect.value;

    try {
        const response = await fetch('/api/emprestimos', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                matricula: parseInt(matricula, 10),
                livrosIds: [parseInt(livroId, 10)]
            })
        });

        if (response.ok) {
            await mostrarMensagem("Empréstimo Realizado", `O empréstimo foi realizado com sucesso!`);
            location.href = '/dashboard';
        } else {
            const errData = await response.json();
            await mostrarMensagem("Erro", `Erro ao realizar empréstimo: ${errData.message || response.statusText}`);
        }
    } catch (error) {
        console.error(error);
        await mostrarMensagem("Erro de Conexão", "Erro de conexão ao tentar realizar o empréstimo.");
    }
}

// Devolver todos os livros de um empréstimo (Formato novo para bibliotecário)
async function devolverEmprestimo(emprestimoId, matricula, livrosIdsStr) {
    if (!emprestimoId || !matricula || !livrosIdsStr) return;
    
    // Converte a string de IDs em um array de inteiros
    const livrosIds = livrosIdsStr.split(',').map(id => parseInt(id.trim(), 10)).filter(id => !isNaN(id));

    try {
        const response = await fetch('/api/emprestimos/devolver', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                emprestimoId: parseInt(emprestimoId, 10),
                matricula: parseInt(matricula, 10),
                livrosIds: livrosIds
            })
        });

        if (response.ok) {
            const card = document.getElementById(`emprestimo-${emprestimoId}`);
            if (card) {
                card.style.opacity = '0';
                card.style.transform = 'translateY(10px)';
                card.style.transition = 'all 0.5s ease';

                setTimeout(async () => {
                    card.remove();
                    await mostrarMensagem("Devolução Concluída", "Empréstimo devolvido com sucesso!");
                    
                    const lista = document.getElementById('lista-devolucoes');
                    const emprestimosRestantes = lista ? lista.getElementsByClassName('livro') : [];
                    if (emprestimosRestantes.length === 0) {
                        const msgVazia = document.getElementById('mensagem-vazia');
                        if (msgVazia) msgVazia.style.display = 'block';
                    }
                }, 500);
            } else {
                await mostrarMensagem("Devolução Concluída", "Empréstimo devolvido com sucesso!");
                location.reload();
            }
        } else {
            const errData = await response.json();
            await mostrarMensagem("Erro", `Erro ao realizar devolução: ${errData.message || response.statusText}`);
        }
    } catch (error) {
        console.error(error);
        await mostrarMensagem("Erro de Conexão", "Erro de conexão ao tentar realizar a devolução.");
    }
}
