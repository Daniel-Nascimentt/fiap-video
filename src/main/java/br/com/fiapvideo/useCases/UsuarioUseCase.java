package br.com.fiapvideo.useCases;

import br.com.fiapvideo.repository.ContaRepository;
import br.com.fiapvideo.repository.UsuarioRepository;
import br.com.fiapvideo.useCases.domain.UsuarioDomain;
import br.com.fiapvideo.web.request.UsuarioRequest;
import br.com.fiapvideo.web.response.UsuarioResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public class UsuarioUseCase {

    public Mono<UsuarioResponse> criarNovoUsuario(UsuarioRequest request, UsuarioRepository usuarioRepository, ContaRepository contaRepository){

        return usuarioRepository.save(
                new UsuarioDomain(
                        request.getNome(),
                        request.getEmail(),
                        request.getDataNascimento(),
                        new ContaUseCase().criarConta(contaRepository).block(),
                        LocalDateTime.now()
                )
        ).map(this::converterDomainParaResponse);
    }

    private UsuarioResponse converterDomainParaResponse(UsuarioDomain usuarioSaved) {
        return new UsuarioResponse(
            usuarioSaved.getNome(),
            usuarioSaved.getEmail(),
            usuarioSaved.getDataNascimento(),
            usuarioSaved.getConta(),
            usuarioSaved.getCadastradoEm()
        );
    }

}
