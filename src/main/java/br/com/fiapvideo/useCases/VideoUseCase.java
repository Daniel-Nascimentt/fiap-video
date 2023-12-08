package br.com.fiapvideo.useCases;

import br.com.fiapvideo.repository.VideoRepository;
import br.com.fiapvideo.useCases.domain.CategoriaDomain;
import br.com.fiapvideo.useCases.domain.PerformanceDomain;
import br.com.fiapvideo.useCases.domain.UsuarioDomain;
import br.com.fiapvideo.useCases.domain.VideoDomain;
import br.com.fiapvideo.web.request.VideoRequest;
import br.com.fiapvideo.web.response.VideoResponse;
import com.mongodb.client.model.mql.MqlArray;
import jakarta.validation.constraints.NotNull;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class VideoUseCase {

    private static String URL_BASE = "https://fiap-video.com/";

    public void criarVideo(@NotNull VideoRequest request, @NotNull UsuarioDomain usuario, @NotNull VideoRepository videoRepository){

        VideoDomain video = new VideoDomain(
                request.getTitulo(),
                gerarUrlAPartirTitulo(request.getTitulo()),
                LocalDateTime.now(),
                new PerformanceDomain(new ArrayList<>(), new ArrayList<>()),
                request.getCategoria(),
                usuario
        );

        videoRepository.save(video).subscribe();

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
}
