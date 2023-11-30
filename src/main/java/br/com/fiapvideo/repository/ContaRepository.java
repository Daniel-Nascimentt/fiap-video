package br.com.fiapvideo.repository;

import br.com.fiapvideo.useCases.domain.ContaDomain;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ContaRepository extends ReactiveMongoRepository<ContaDomain, String> {
}
