package br.com.fiapvideo.useCases;

import br.com.fiapvideo.repository.ContaRepository;
import br.com.fiapvideo.repository.UsuarioRepository;
import br.com.fiapvideo.useCases.domain.UsuarioDomain;
import br.com.fiapvideo.web.request.UsuarioRequest;
import br.com.fiapvideo.web.response.UsuarioResponse;

import java.time.LocalDateTime;

public class UsuarioUseCase {

    public UsuarioResponse criarNovoUsuario(UsuarioRequest request, UsuarioRepository usuarioRepository, ContaRepository contaRepository){

        UsuarioDomain usuarioSaved = usuarioRepository.save(
                new UsuarioDomain(
                        request.getNome(),
                        request.getEmail(),
                        request.getDataNascimento(),
                        new ContaUseCase().criarConta(contaRepository),
                        LocalDateTime.now()
                )
        );

        return this.converterDomainParaResponse(usuarioSaved);

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
