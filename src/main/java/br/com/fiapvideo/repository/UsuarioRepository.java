package br.com.fiapvideo.repository;

import br.com.fiapvideo.useCases.domain.UsuarioDomain;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UsuarioRepository extends ReactiveMongoRepository<UsuarioDomain, String> {
}
