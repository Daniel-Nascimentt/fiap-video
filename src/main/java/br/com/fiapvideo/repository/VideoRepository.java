package br.com.fiapvideo.repository;

import br.com.fiapvideo.useCases.domain.VideoDomain;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface VideoRepository extends ReactiveMongoRepository<VideoDomain, String> {
}
