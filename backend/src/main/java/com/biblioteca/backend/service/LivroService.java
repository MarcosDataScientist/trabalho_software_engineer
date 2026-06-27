package com.biblioteca.backend.service;

import com.biblioteca.backend.dto.AreaRequest;
import com.biblioteca.backend.dto.AreaResponse;
import com.biblioteca.backend.dto.AutorRequest;
import com.biblioteca.backend.dto.AutorResponse;
import com.biblioteca.backend.dto.LivroRequest;
import com.biblioteca.backend.dto.LivroResponse;
import com.biblioteca.backend.exception.BusinessRuleException;
import com.biblioteca.backend.exception.ResourceNotFoundException;
import com.biblioteca.backend.repository.AreaRepository;
import com.biblioteca.backend.repository.AutorRepository;
import com.biblioteca.backend.repository.LivroRepository;
import com.biblioteca.backend.repository.TituloRepository;
import com.biblioteca.backend.model.Area;
import com.biblioteca.backend.model.Autor;
import com.biblioteca.backend.model.Livro;
import com.biblioteca.backend.model.Titulo;
import com.biblioteca.backend.model.TituloAutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Transactional
public class LivroService {

    private final LivroRepository livroRepository;
    private final TituloRepository tituloRepository;
    private final AreaRepository areaRepository;
    private final AutorRepository autorRepository;

    public LivroService(
            LivroRepository livroRepository,
            TituloRepository tituloRepository,
            AreaRepository areaRepository,
            AutorRepository autorRepository
    ) {
        this.livroRepository = livroRepository;
        this.tituloRepository = tituloRepository;
        this.areaRepository = areaRepository;
        this.autorRepository = autorRepository;
    }

    @Transactional(readOnly = true)
    public List<LivroResponse> listarTodos() {
        return livroRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<LivroResponse> listarDisponiveis() {
        return livroRepository.findByDisponivelTrue().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public LivroResponse buscarPorId(Long id) {
        return toResponse(buscarLivro(id));
    }

    public LivroResponse cadastrar(LivroRequest request) {
        Titulo titulo = resolverTitulo(request);
        vincularAutores(titulo, request.autorIds());

        Livro livro = new Livro(
                request.disponivel() != null ? request.disponivel() : true,
                request.exemplarBiblioteca() != null ? request.exemplarBiblioteca() : true,
                titulo
        );

        return toResponse(livroRepository.save(livro));
    }

    public LivroResponse atualizar(Long id, LivroRequest request) {
        Livro livro = buscarLivro(id);
        Titulo titulo = livro.getTitulo();

        if (request.tituloId() != null && !request.tituloId().equals(titulo.getId())) {
            throw new BusinessRuleException("Não é permitido alterar o título vinculado a um exemplar existente");
        }

        atualizarDadosTitulo(titulo, request);
        if (request.areaId() != null) {
            titulo.setArea(buscarArea(request.areaId()));
        }
        vincularAutores(titulo, request.autorIds());

        if (request.disponivel() != null) {
            livro.setDisponivel(request.disponivel());
        }
        if (request.exemplarBiblioteca() != null) {
            livro.setExemplarBiblioteca(request.exemplarBiblioteca());
        }

        tituloRepository.save(titulo);
        return toResponse(livroRepository.save(livro));
    }

    public void excluir(Long id) {
        Livro livro = buscarLivro(id);
        livroRepository.delete(livro);
    }

    @Transactional(readOnly = true)
    public List<AreaResponse> listarAreas() {
        return areaRepository.findAll().stream()
                .map(this::toAreaResponse)
                .toList();
    }

    public AreaResponse cadastrarArea(AreaRequest request) {
        Area area = new Area(request.nome(), request.descricao());
        return toAreaResponse(areaRepository.save(area));
    }

    @Transactional(readOnly = true)
    public List<AutorResponse> listarAutores() {
        return autorRepository.findAll().stream()
                .map(this::toAutorResponse)
                .toList();
    }

    public AutorResponse cadastrarAutor(AutorRequest request) {
        Autor autor = new Autor(request.nome(), request.sobrenome(), request.titulacao());
        return toAutorResponse(autorRepository.save(autor));
    }

    private Titulo resolverTitulo(LivroRequest request) {
        if (request.tituloId() != null) {
            return buscarTitulo(request.tituloId());
        }

        validarNovoTitulo(request);
        Titulo titulo = new Titulo(
                request.titulo(),
                request.prazo(),
                request.isbn(),
                request.edicao(),
                request.ano(),
                buscarArea(request.areaId())
        );
        return tituloRepository.save(titulo);
    }

    private void validarNovoTitulo(LivroRequest request) {
        if (request.areaId() == null) {
            throw new BusinessRuleException("A área é obrigatória ao cadastrar um novo título");
        }
        if (!StringUtils.hasText(request.titulo())) {
            throw new BusinessRuleException("O nome do título é obrigatório ao cadastrar um novo livro");
        }
        if (request.prazo() == null || request.prazo() <= 0) {
            throw new BusinessRuleException("O prazo de empréstimo é obrigatório e deve ser maior que zero");
        }
        if (StringUtils.hasText(request.isbn()) && tituloRepository.existsByIsbn(request.isbn())) {
            throw new BusinessRuleException("Já existe um título cadastrado com o ISBN informado");
        }
    }

    private void atualizarDadosTitulo(Titulo titulo, LivroRequest request) {
        if (StringUtils.hasText(request.titulo())) {
            titulo.setTitulo(request.titulo());
        }
        if (request.prazo() != null) {
            if (request.prazo() <= 0) {
                throw new BusinessRuleException("O prazo deve ser maior que zero");
            }
            titulo.setPrazo(request.prazo());
        }
        if (request.isbn() != null) {
            tituloRepository.findByIsbn(request.isbn())
                    .filter(existing -> !existing.getId().equals(titulo.getId()))
                    .ifPresent(existing -> {
                        throw new BusinessRuleException("Já existe um título cadastrado com o ISBN informado");
                    });
            titulo.setIsbn(request.isbn());
        }
        if (request.edicao() != null) {
            titulo.setEdicao(request.edicao());
        }
        if (request.ano() != null) {
            titulo.setAno(request.ano());
        }
    }

    private void vincularAutores(Titulo titulo, List<Long> autorIds) {
        if (autorIds == null || autorIds.isEmpty()) {
            return;
        }

        titulo.getAutores().clear();
        for (Long autorId : autorIds) {
            Autor autor = autorRepository.findById(autorId)
                    .orElseThrow(() -> new ResourceNotFoundException("Autor não encontrado com id " + autorId));
            TituloAutor vinculo = new TituloAutor(titulo, autor);
            titulo.getAutores().add(vinculo);
        }
        tituloRepository.save(titulo);
    }

    private Livro buscarLivro(Long id) {
        return livroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado com id " + id));
    }

    private Titulo buscarTitulo(Long id) {
        return tituloRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Título não encontrado com id " + id));
    }

    private Area buscarArea(Long id) {
        return areaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Área não encontrada com id " + id));
    }

    private LivroResponse toResponse(Livro livro) {
        Titulo titulo = livro.getTitulo();
        Area area = titulo.getArea();

        List<AutorResponse> autores = titulo.getAutores().stream()
                .map(TituloAutor::getAutor)
                .map(this::toAutorResponse)
                .toList();

        return new LivroResponse(
                livro.getId(),
                livro.getDisponivel(),
                livro.getExemplarBiblioteca(),
                titulo.getId(),
                titulo.getTitulo(),
                titulo.getPrazo(),
                titulo.getIsbn(),
                titulo.getEdicao(),
                titulo.getAno(),
                area.getId(),
                area.getNome(),
                autores
        );
    }

    private AreaResponse toAreaResponse(Area area) {
        return new AreaResponse(area.getId(), area.getNome(), area.getDescricao());
    }

    private AutorResponse toAutorResponse(Autor autor) {
        return new AutorResponse(autor.getId(), autor.getNome(), autor.getSobrenome(), autor.getTitulacao());
    }
}
