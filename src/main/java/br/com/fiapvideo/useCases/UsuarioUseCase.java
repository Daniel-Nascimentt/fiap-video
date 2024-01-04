package br.com.fiapvideo.useCases;

import br.com.fiapvideo.exceptions.UsuarioNotFoundException;
import br.com.fiapvideo.repository.ContaRepository;
import br.com.fiapvideo.repository.UsuarioRepository;
import br.com.fiapvideo.useCases.domain.ContaDomain;
import br.com.fiapvideo.useCases.domain.UsuarioDomain;
import br.com.fiapvideo.web.request.UsuarioRequest;
import br.com.fiapvideo.web.response.ContaResponse;
import br.com.fiapvideo.web.response.ToResponse;
import br.com.fiapvideo.web.response.UsuarioResponse;
import jakarta.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public class UsuarioUseCase implements ToResponse<UsuarioDomain, UsuarioResponse> {

    public Mono<UsuarioResponse> criarNovoUsuario(@NotNull UsuarioRequest request, @NotNull UsuarioRepository usuarioRepository, @NotNull ContaRepository contaRepository){

        Mono<ContaDomain> contaMono = new ContaUseCase().criarConta(contaRepository);

        return contaMono.flatMap(conta -> {
            return usuarioRepository.save(
                    new UsuarioDomain(
                            request.getNome(),
                            request.getEmail(),
                            request.getDataNascimento(),
                            conta,
                            LocalDateTime.now()
                    )
            ).map(this::toResponse);
        });

    }

    public Mono<UsuarioDomain> buscarPorEmail(String email, UsuarioRepository usuarioRepository) throws UsuarioNotFoundException {
        return usuarioRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new UsuarioNotFoundException()));
    }

    public Mono<UsuarioResponse> atualizarUsuario(Mono<UsuarioDomain> usuarioEncontrado, UsuarioRequest request, UsuarioRepository usuarioRepository) {
        return usuarioEncontrado.flatMap(user -> {
            this.atualizarAtributos(user, request);
            return usuarioRepository.save(user);
        }).map(this::toResponseUpdate);

    }

    private void atualizarAtributos(UsuarioDomain usuario, UsuarioRequest request) {
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setDataNascimento(request.getDataNascimento());
    }

    public Mono<Void> removerUsuarioPorEmail(UsuarioDomain usuario, UsuarioRepository usuarioRepository, ContaRepository contaRepository) {
        new ContaUseCase().removerConta(usuario.getConta(), contaRepository);
        return usuarioRepository.delete(usuario);
    }

    @Override
    public UsuarioResponse toResponse(UsuarioDomain usuarioSaved) {

        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(usuarioSaved, UsuarioResponse.class);

    }

    @Override
    public UsuarioResponse toResponseUpdate(UsuarioDomain usuarioSaved) {
        ModelMapper modelMapper = new ModelMapper();

        TypeMap<ContaDomain, ContaResponse> typeMap = modelMapper.createTypeMap(ContaDomain.class, ContaResponse.class);

        typeMap.addMappings(mapper -> {
            mapper.skip(ContaResponse::setId);
            mapper.skip(ContaResponse::setFavoritos);
            mapper.skip(ContaResponse::setVideosAssistidos);
            mapper.skip(ContaResponse::setVideosPublicados);
        });

        return modelMapper.map(usuarioSaved, UsuarioResponse.class);
    }

}
