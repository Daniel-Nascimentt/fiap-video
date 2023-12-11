package br.com.fiapvideo.service;


import br.com.fiapvideo.exceptions.UsuarioNotFoundException;
import br.com.fiapvideo.exceptions.VideoNotFoundException;
import br.com.fiapvideo.filters.AbstractFilter;
import br.com.fiapvideo.filters.conditions.VideoFilterConditions;
import br.com.fiapvideo.repository.ContaRepository;
import br.com.fiapvideo.repository.VideoRepository;
import br.com.fiapvideo.useCases.VideoUseCase;
import br.com.fiapvideo.useCases.domain.UsuarioDomain;
import br.com.fiapvideo.useCases.domain.VideoDomain;
import br.com.fiapvideo.web.request.VideoRequest;
import br.com.fiapvideo.web.request.EspectVideoRequest;
import br.com.fiapvideo.web.response.VideoResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public void criarVideo(@NotNull VideoRequest request) throws UsuarioNotFoundException {

        usuarioService.buscarPorEmail(request.getPublicadoPor()).subscribe(user -> {
            new VideoUseCase().criarVideo(request, user, videoRepository, contaRepository);
       });

    }

    public Flux<VideoResponse> buscarVideosDinamicamente(AbstractFilter filter) {

        Pageable pageable = filter.getPageableAndSort();

        Query query = new Query().with(pageable);
        query.addCriteria(filter.getCriteria(new VideoFilterConditions(filter.getParameters())));

        return new VideoUseCase().buscarVideosPaginados(reactiveMongoTemplate, query);

    }

    public void visualizarVideo(EspectVideoRequest request) throws UsuarioNotFoundException{
        Mono<UsuarioDomain> usuario = usuarioService.buscarPorEmail(request.getEmailTelespectador());
        Mono<VideoDomain> video = videoRepository.findById(request.getVideoId()).switchIfEmpty(Mono.error(new VideoNotFoundException()));

        new VideoUseCase().visualizarVideo(usuario.block(), video.block(), contaRepository, reactiveMongoTemplate);
    }

    public void favoritarVideo(EspectVideoRequest request) {
        Mono<UsuarioDomain> usuario = usuarioService.buscarPorEmail(request.getEmailTelespectador());
        Mono<VideoDomain> video = videoRepository.findById(request.getVideoId()).switchIfEmpty(Mono.error(new VideoNotFoundException()));

        new VideoUseCase().favoritarVideo(usuario.block(), video.block(), contaRepository, reactiveMongoTemplate);
    }
}
