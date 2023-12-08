package br.com.fiapvideo.useCases;

import br.com.fiapvideo.exceptions.UsuarioNotFoundException;
import br.com.fiapvideo.repository.ContaRepository;
import br.com.fiapvideo.repository.UsuarioRepository;
import br.com.fiapvideo.useCases.domain.ContaDomain;
import br.com.fiapvideo.useCases.domain.UsuarioDomain;
import br.com.fiapvideo.web.request.UsuarioRequest;
import br.com.fiapvideo.web.response.UsuarioResponse;
import jakarta.validation.constraints.NotNull;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public class UsuarioUseCase {

    public Mono<UsuarioResponse> criarNovoUsuario(@NotNull UsuarioRequest request, @NotNull UsuarioRepository usuarioRepository, @NotNull ContaRepository contaRepository){

        return usuarioRepository.save(
                new UsuarioDomain(
                        request.getNome(),
                        request.getEmail(),
                        request.getDataNascimento(),
                        criarNovaConta(contaRepository),
                        LocalDateTime.now()
                )
        ).map(this::converterDomainParaResponse);
    }

    private ContaDomain criarNovaConta(ContaRepository contaRepository) {
        return new ContaUseCase().criarConta(contaRepository).block();
    }

    public UsuarioResponse converterDomainParaResponse(UsuarioDomain usuarioSaved) {
        return new UsuarioResponse(
            usuarioSaved.getNome(),
            usuarioSaved.getEmail(),
            usuarioSaved.getDataNascimento(),
            usuarioSaved.getConta(),
            usuarioSaved.getCadastradoEm()
        );
    }

    public Mono<UsuarioDomain> buscarPorEmail(String email, UsuarioRepository usuarioRepository) throws UsuarioNotFoundException {
        return usuarioRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new UsuarioNotFoundException()));
    }

    public Mono<UsuarioResponse> atualizarUsuario(Mono<UsuarioDomain> usuarioEncontrado, UsuarioRequest request, UsuarioRepository usuarioRepository) {
        return usuarioRepository.save(usuarioEncontrado.map(user -> atualizarAtributos(user, request)).block()).map(this::converterDomainParaResponse);
    }

    private UsuarioDomain atualizarAtributos(UsuarioDomain usuario, UsuarioRequest request) {
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setDataNascimento(request.getDataNascimento());

        return usuario;
    }

    public void removerUsuarioPorEmail(UsuarioDomain usuario, UsuarioRepository usuarioRepository, ContaRepository contaRepository) {
        new ContaUseCase().removerConta(usuario.getConta(), contaRepository);
        usuarioRepository.delete(usuario).subscribe();
    }
}
