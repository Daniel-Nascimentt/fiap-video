package br.com.fiapvideo.useCases;

import br.com.fiapvideo.exceptions.PublicadorNaoCorrespondeException;
import br.com.fiapvideo.exceptions.VideoNotFoundException;
import br.com.fiapvideo.repository.ContaRepository;
import br.com.fiapvideo.repository.VideoRepository;
import br.com.fiapvideo.useCases.domain.PerformanceDomain;
import br.com.fiapvideo.useCases.domain.UsuarioDomain;
import br.com.fiapvideo.useCases.domain.VideoDomain;
import br.com.fiapvideo.web.request.VideoRequest;
import br.com.fiapvideo.web.response.*;
import jakarta.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.spi.MappingContext;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

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
                request.getDescricao(),
                usuario
        )).doOnNext(video -> {
            new ContaUseCase().addVideoPublicado(usuario.getConta(), contaRepository, video);
        });

    }

    private String gerarUrlAPartirTitulo(String titulo) {
        String tituloSemEspaco = titulo.replace(" ", "");
        return URL_BASE.concat(UUID.randomUUID().toString()).concat("/").concat(tituloSemEspaco);
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

        modelMapper.typeMap(PerformanceDomain.class, PerformanceResponse.class)
                .addMapping(PerformanceDomain::getMarcadoFavorito, PerformanceResponse::setMarcadoFavorito)
                .addMapping(PerformanceDomain::getVisualizacoes, PerformanceResponse::setVisualizacoes)
                .setPostConverter(MappingContext::getDestination);

        return modelMapper.map(videoSaved, VideoResponse.class);
    }

    @Override
    public VideoResponse toResponseUpdate(VideoDomain videoSaved) {

        ModelMapper modelMapper = new ModelMapper();

        TypeMap<UsuarioDomain, UsuarioResponse> typeMap = modelMapper.createTypeMap(UsuarioDomain.class, UsuarioResponse.class);

        typeMap.addMappings(mapper -> {
            mapper.skip(UsuarioResponse::setEmail);
            mapper.skip(UsuarioResponse::setCadastradoEm);
            mapper.skip(UsuarioResponse::setDataNascimento);
            mapper.skip(UsuarioResponse::setNome);
        });

        return modelMapper.map(videoSaved, VideoResponse.class);

    }

    public Mono<Void> excluirVideo(String videoId, VideoRepository videoRepository) {
        Mono<VideoDomain> video = videoRepository.findById(videoId)
                .switchIfEmpty(Mono.error(new VideoNotFoundException()));

        return video.flatMap(videoRepository::delete);
    }

    public Mono<VideoResponse> atualizarVideo(VideoRequest request, String videoId, VideoRepository videoRepository) {
        Mono<VideoDomain> video = videoRepository.findById(videoId).switchIfEmpty(Mono.error(new VideoNotFoundException()));

        return validarPublicadorParaAtualizar(request, video)
                .flatMap(videoRepository::save)
                .map(this::toResponseUpdate);
    }

    private Mono<VideoDomain> validarPublicadorParaAtualizar(VideoRequest request, Mono<VideoDomain> video) {
        return video.flatMap(videoDomain -> {
            if (!videoDomain.getPublicadoPor().getEmail().equals(request.getPublicadoPor())) {
                return Mono.error(new PublicadorNaoCorrespondeException("E-mail do publicador não corresponde."));
            }
            return Mono.just(this.atualizarAtributos(videoDomain, request));
        });
    }

    private VideoDomain atualizarAtributos(VideoDomain videoDomain, VideoRequest request) {
        videoDomain.setCategoria(request.getCategoria());
        videoDomain.setTitulo(request.getTitulo());
        videoDomain.setUrl(gerarUrlAPartirTitulo(request.getTitulo()));
        videoDomain.setDataPublicacao(LocalDateTime.now());
        videoDomain.setDescricao(request.getDescricao());

        return videoDomain;
    }

    public Mono<RelatorioVideoResponse> calcularEstatisticasVideos(ReactiveMongoTemplate reactiveMongoTemplate) {
        return reactiveMongoTemplate.aggregate(
                newAggregation(VideoDomain.class,
                        group().count().as("totalVideos")
                                .sum("performance.marcadoFavorito").as("totalMarcadoFavorito")
                                .sum("performance.visualizacoes").as("totalViews")
                                .avg("performance.visualizacoes").as("mediaViewsPorVideo")
                ), "videos", RelatorioVideoResponse.class
        ).singleOrEmpty();
    }

}
