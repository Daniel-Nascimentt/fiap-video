package br.com.fiapvideo.repository;

import br.com.fiapvideo.useCases.domain.ContaDomain;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContaRepository extends MongoRepository<ContaDomain, String> {
}
