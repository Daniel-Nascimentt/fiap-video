package br.com.fiapvideo.repository;

import br.com.fiapvideo.useCases.domain.UsuarioDomain;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UsuarioRepository extends ReactiveMongoRepository<UsuarioDomain, String> {
    Mono<UsuarioDomain> findByEmail(String email);
}
