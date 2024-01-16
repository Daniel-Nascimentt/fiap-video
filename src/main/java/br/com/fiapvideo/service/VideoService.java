package br.com.fiapvideo.service;


import br.com.fiapvideo.exceptions.UsuarioNotFoundException;
import br.com.fiapvideo.exceptions.VideoNotFoundException;
import br.com.fiapvideo.filters.AbstractFilter;
import br.com.fiapvideo.filters.conditions.RecomendacaoFilterConditions;
import br.com.fiapvideo.filters.conditions.VideoFilterConditions;
import br.com.fiapvideo.repository.ContaRepository;
import br.com.fiapvideo.repository.VideoRepository;
import br.com.fiapvideo.useCases.VideoUseCase;
import br.com.fiapvideo.web.request.VideoRequest;
import br.com.fiapvideo.web.request.EspectVideoRequest;
import br.com.fiapvideo.web.response.RelatorioVideoResponse;
import br.com.fiapvideo.web.response.VideoResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class VideoService {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    public Mono<VideoResponse> criarVideo(@NotNull VideoRequest request) throws UsuarioNotFoundException {

        return usuarioService.buscarPorEmail(request.getPublicadoPor())
                .flatMap(user -> new VideoUseCase().criarVideo(request, user, videoRepository, contaRepository))
                .flatMap(videoDomain -> Mono.just(new VideoUseCase().toResponse(videoDomain)));

    }

    public Flux<VideoResponse> buscarVideosDinamicamente(AbstractFilter filter) {

        Pageable pageable = filter.getPageableAndSort();

        Query query = new Query().with(pageable);
        query.addCriteria(filter.getCriteria(new VideoFilterConditions(filter.getParameters())));

        return new VideoUseCase().buscarVideosPaginados(reactiveMongoTemplate, query);

    }

    public Mono<Void> visualizarVideo(EspectVideoRequest request) {
        return usuarioService.buscarPorEmail(request.getEmailTelespectador())
                .switchIfEmpty(Mono.error(new UsuarioNotFoundException()))
                .flatMap(usuarioDomain -> videoRepository.findById(request.getVideoId())
                        .switchIfEmpty(Mono.error(new VideoNotFoundException()))
                        .flatMap(videoDomain -> {
                            new VideoUseCase().visualizarVideo(usuarioDomain, videoDomain, contaRepository, reactiveMongoTemplate);
                            return Mono.empty();
                        })
                )
                .then();
    }

    public Mono<Void> favoritarVideo(EspectVideoRequest request) {
        return usuarioService.buscarPorEmail(request.getEmailTelespectador())
                .switchIfEmpty(Mono.error(new UsuarioNotFoundException()))
                .flatMap(usuarioDomain -> videoRepository.findById(request.getVideoId())
                        .switchIfEmpty(Mono.error(new VideoNotFoundException()))
                        .flatMap(videoDomain -> {
                            new VideoUseCase().favoritarVideo(usuarioDomain, videoDomain, contaRepository, reactiveMongoTemplate);
                            return Mono.empty();
                        })
                )
                .then();
    }

    public Mono<Void> excluirVideo(String videoId) {
        return new VideoUseCase().excluirVideo(videoId, videoRepository);
    }

    public Mono<VideoResponse> atualizarVideo(VideoRequest request, String videoId) {
        return new VideoUseCase().atualizarVideo(request, videoId, videoRepository);
    }

    public Flux<VideoResponse> recomendacaoTopVideosPorCategoriaFavoritada(String emailUsuario, AbstractFilter filter) {

        Pageable pageable = filter.getDefaultPageableAndSort();

        Query query = new Query().with(pageable);
        query.limit(5).with(Sort.by(Sort.Direction.DESC, "performance.visualizacoes"));

        return usuarioService.buscarPorEmail(emailUsuario)
                .flatMapMany(user -> {
                    query.addCriteria(filter.getCriteria(new RecomendacaoFilterConditions(user.getId(), user.ultimoVideoFavoritado())));
                    return new VideoUseCase().buscarVideosPaginados(reactiveMongoTemplate, query);
                });



    }

    public Mono<RelatorioVideoResponse> calcularEstatisticasVideos(){
        return new VideoUseCase().calcularEstatisticasVideos(this.reactiveMongoTemplate);
    }
}
