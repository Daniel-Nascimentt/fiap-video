package br.com.fiapvideo.repository;

import br.com.fiapvideo.useCases.domain.UsuarioDomain;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsuarioRepository extends MongoRepository<UsuarioDomain, String> {
}
