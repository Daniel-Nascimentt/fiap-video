package br.com.fiapvideo.service;

import br.com.fiapvideo.exceptions.UsuarioNotFoundException;
import br.com.fiapvideo.repository.ContaRepository;
import br.com.fiapvideo.repository.UsuarioRepository;
import br.com.fiapvideo.useCases.UsuarioUseCase;
import br.com.fiapvideo.useCases.domain.UsuarioDomain;
import br.com.fiapvideo.web.request.UsuarioRequest;
import br.com.fiapvideo.web.response.UsuarioResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ContaRepository contaRepository;

    @Transactional
    public Mono<UsuarioResponse> criarNovoUsuario(UsuarioRequest request) {
        return new UsuarioUseCase().criarNovoUsuario(request, usuarioRepository, contaRepository);
    }

    @Transactional
    public Mono<UsuarioDomain> buscarPorEmail(String email) throws UsuarioNotFoundException {
        return new UsuarioUseCase().buscarPorEmail(email, usuarioRepository);
    }

    public UsuarioResponse converterDomainParaResponse(UsuarioDomain usuario) {
        return new UsuarioUseCase().converterDomainParaResponse(usuario);
    }

    @Transactional
    public Mono<UsuarioResponse> atualizarUsuario(String emailAtual, UsuarioRequest request) {
        Mono<UsuarioDomain> usuarioEncontrado = this.buscarPorEmail(emailAtual);
        return new UsuarioUseCase().atualizarUsuario(usuarioEncontrado, request, usuarioRepository);
    }

    @Transactional
    public void removerUsuarioPorEmail(String email) {
       new UsuarioUseCase().removerUsuarioPorEmail(this.buscarPorEmail(email).block(), usuarioRepository, contaRepository);
    }
}
