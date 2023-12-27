package br.com.fiapvideo.useCases;

import br.com.fiapvideo.repository.ContaRepository;
import br.com.fiapvideo.repository.VideoRepository;
import br.com.fiapvideo.useCases.domain.PerformanceDomain;
import br.com.fiapvideo.useCases.domain.UsuarioDomain;
import br.com.fiapvideo.useCases.domain.VideoDomain;
import br.com.fiapvideo.web.request.VideoRequest;
import br.com.fiapvideo.web.response.VideoResponse;
import jakarta.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public class VideoUseCase implements ToResponse<VideoDomain, VideoResponse> {

    private static final Long ADD_UNIQUE_VIEW = 1L;
    private static final Long ADD_UNIQUE_FAVORITADO = 1L;
    private final static String URL_BASE = "https://fiap-video.com/";

    public Mono<VideoDomain> criarVideo(@NotNull VideoRequest request, @NotNull UsuarioDomain usuario, @NotNull VideoRepository videoRepository, ContaRepository contaRepository){

        return videoRepository.save(new VideoDomain(
                request.getTitulo(),
                gerarUrlAPartirTitulo(request.getTitulo()),
                LocalDateTime.now(),
                new PerformanceDomain(0L, 0L),
                request.getCategoria(),
                usuario
        )).doOnNext(video -> {
            new ContaUseCase().addVideoPublicado(usuario.getConta(), contaRepository, video);
        });

    }

    private String gerarUrlAPartirTitulo(String titulo) {
        String tituloSemEspaco = titulo.replace(" ", "");
        return URL_BASE.concat(tituloSemEspaco);
    }

    public Flux<VideoResponse> buscarVideosPaginados(ReactiveMongoTemplate reactiveMongoTemplate, Query query) {
        return reactiveMongoTemplate.find(query, VideoDomain.class).map(this::toResponse);
    }

    private void incrementarView(VideoDomain videoDomain, ReactiveMongoTemplate mongoTemplate) {

        Query query = new Query(Criteria.where("_id").is(videoDomain.getId()));
        Update update = new Update().inc("performance.visualizacoes", ADD_UNIQUE_VIEW);

        mongoTemplate.updateFirst(query, update, VideoDomain.class).subscribe();
    }

    public void visualizarVideo(UsuarioDomain usuarioDomain, VideoDomain videoDomain, ContaRepository contaRepository, ReactiveMongoTemplate mongoTemplate){
        new ContaUseCase().addVideoHistoricoAssistido(usuarioDomain.getConta(), videoDomain, contaRepository);
        this.incrementarView(videoDomain, mongoTemplate);
    }

    public void favoritarVideo(UsuarioDomain usuarioDomain, VideoDomain videoDomain, ContaRepository contaRepository, ReactiveMongoTemplate mongoTemplate){
        new ContaUseCase().addVideoHistoricoFavoritos(usuarioDomain.getConta(), videoDomain, contaRepository);
        this.incrementFavoritado(videoDomain, mongoTemplate);
    }

    private void incrementFavoritado(VideoDomain videoDomain, ReactiveMongoTemplate mongoTemplate) {
        Query query = new Query(Criteria.where("_id").is(videoDomain.getId()));
        Update update = new Update().inc("performance.marcadoFavorito", ADD_UNIQUE_FAVORITADO);

        mongoTemplate.updateFirst(query, update, VideoDomain.class).subscribe();
    }

    @Override
    public VideoResponse toResponse(VideoDomain videoSaved) {
        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(videoSaved, VideoResponse.class);
    }

    @Override
    public VideoResponse toResponseUpdate(VideoDomain domain) {
        return null;
    }
}
