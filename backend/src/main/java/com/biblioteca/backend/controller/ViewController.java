package com.biblioteca.backend.controller;

import com.biblioteca.backend.model.Aluno;
import com.biblioteca.backend.repository.AlunoRepository;
import com.biblioteca.backend.service.EmprestimoService;
import com.biblioteca.backend.service.LivroService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ViewController {

    private final AlunoRepository alunoRepository;
    private final LivroService livroService;
    private final EmprestimoService emprestimoService;

    public ViewController(AlunoRepository alunoRepository, LivroService livroService, EmprestimoService emprestimoService) {
        this.alunoRepository = alunoRepository;
        this.livroService = livroService;
        this.emprestimoService = emprestimoService;
    }

    @GetMapping("/")
    public String index(Model model) {
        return "redirect:/dashboard";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("isLoginPage", true);
        return "logar-aluno";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam Long matricula, @RequestParam String nome, HttpSession session, RedirectAttributes redirectAttributes) {
        var alunoOpt = alunoRepository.findById(matricula);
        if (alunoOpt.isPresent() && alunoOpt.get().getNome().equalsIgnoreCase(nome.trim())) {
            session.setAttribute("alunoMatricula", matricula);
            return "redirect:/dashboard";
        } else {
            redirectAttributes.addFlashAttribute("error", "Matrícula ou Nome incorretos!");
            return "redirect:/login";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        var aluno = getLoggedAluno(session);
        model.addAttribute("aluno", aluno);
        model.addAttribute("livros", livroService.listarTodos());
        return "index";
    }

    @GetMapping("/emprestar")
    public String emprestar(Model model, HttpSession session) {
        var aluno = getLoggedAluno(session);
        model.addAttribute("aluno", aluno);
        model.addAttribute("alunos", alunoRepository.findAll());
        model.addAttribute("livrosDisponiveis", livroService.listarDisponiveis());
        return "emprestar";
    }

    @GetMapping("/devolver")
    public String devolver(Model model, HttpSession session) {
        var aluno = getLoggedAluno(session);
        model.addAttribute("aluno", aluno);
        model.addAttribute("emprestimos", emprestimoService.listarTodos());
        return "devolver";
    }

    public record LivroAgrupado(String titulo, String isbn, long quantidadeTotal, long quantidadeDisponivel) {}

    @GetMapping("/unidades")
    public String unidades(Model model, HttpSession session) {
        var aluno = getLoggedAluno(session);
        model.addAttribute("aluno", aluno);
        
        java.util.List<com.biblioteca.backend.dto.LivroResponse> todos = livroService.listarTodos();
        java.util.Map<String, LivroAgrupado> agrupado = new java.util.HashMap<>();
        
        for (com.biblioteca.backend.dto.LivroResponse l : todos) {
            String key = l.tituloId() + "-" + l.titulo();
            agrupado.putIfAbsent(key, new LivroAgrupado(l.titulo(), l.isbn(), 0, 0));
            var current = agrupado.get(key);
            agrupado.put(key, new LivroAgrupado(
                current.titulo(),
                current.isbn(),
                current.quantidadeTotal() + 1,
                current.quantidadeDisponivel() + (l.disponivel() != null && l.disponivel() ? 1 : 0)
            ));
        }
        
        model.addAttribute("unidades", agrupado.values());
        return "unidades";
    }

    @GetMapping("/cadastro-aluno")
    public String cadastroAluno(Model model, HttpSession session) {
        var aluno = getLoggedAluno(session);
        model.addAttribute("aluno", aluno);
        return "cadastro-aluno";
    }

    @GetMapping("/cadastro-livro")
    public String cadastroLivro(Model model, HttpSession session) {
        var aluno = getLoggedAluno(session);
        model.addAttribute("aluno", aluno);
        return "cadastro-livro";
    }

    @GetMapping("/perfil")
    public String perfil(Model model, HttpSession session) {
        var aluno = getLoggedAluno(session);
        if (aluno == null) {
            return "redirect:/login";
        }
        model.addAttribute("aluno", aluno);
        return "perfil";
    }

    private Aluno getLoggedAluno(HttpSession session) {
        Long matricula = (Long) session.getAttribute("alunoMatricula");
        if (matricula == null) {
            return null;
        }
        return alunoRepository.findById(matricula).orElse(null);
    }
}
