package br.com.fiapvideo.useCases;

import br.com.fiapvideo.repository.ContaRepository;
import br.com.fiapvideo.repository.VideoRepository;
import br.com.fiapvideo.useCases.domain.PerformanceDomain;
import br.com.fiapvideo.useCases.domain.UsuarioDomain;
import br.com.fiapvideo.useCases.domain.VideoDomain;
import br.com.fiapvideo.web.request.VideoRequest;
import br.com.fiapvideo.web.response.VideoResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

public class VideoUseCase {

    private static final Long ADD_UNIQUE_VIEW = 1L;
    private static final Long ADD_UNIQUE_FAVORITADO = 1L;
    private static String URL_BASE = "https://fiap-video.com/";

    public void criarVideo(@NotNull VideoRequest request, @NotNull UsuarioDomain usuario, @NotNull VideoRepository videoRepository, ContaRepository contaRepository){

        VideoDomain video = new VideoDomain(
                request.getTitulo(),
                gerarUrlAPartirTitulo(request.getTitulo()),
                LocalDateTime.now(),
                new PerformanceDomain(0L, 0L),
                request.getCategoria(),
                usuario
        );


        videoRepository.save(video).subscribe(videoSaved -> {
                new ContaUseCase().addVideoPublicado(usuario.getConta(), contaRepository, videoSaved);
                }
        );

    }

    private String gerarUrlAPartirTitulo(String titulo) {
        String tituloSemEspaco = titulo.replace(" ", "");
        return URL_BASE.concat(tituloSemEspaco);
    }

    public Flux<VideoResponse> buscarVideosPaginados(ReactiveMongoTemplate reactiveMongoTemplate, Query query) {
        return reactiveMongoTemplate.find(query, VideoDomain.class).map(this::converterDomainParaResponse);
    }

    private VideoResponse converterDomainParaResponse(VideoDomain video) {
        return new VideoResponse(
                video.getCategoria(),
                video.getUrl(),
                video.getDataPublicacao(),
                video.getPerformance(),
                video.getTitulo(),
                video.getEmailPublicador()
        );
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
}
