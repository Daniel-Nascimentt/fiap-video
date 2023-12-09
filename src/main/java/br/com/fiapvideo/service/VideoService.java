package br.com.fiapvideo.service;


import br.com.fiapvideo.exceptions.UsuarioNotFoundException;
import br.com.fiapvideo.filters.AbstractFilter;
import br.com.fiapvideo.repository.VideoRepository;
import br.com.fiapvideo.useCases.VideoUseCase;
import br.com.fiapvideo.web.request.VideoRequest;
import br.com.fiapvideo.web.response.VideoResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;

@Service
public class VideoService {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    public void criarVideo(@NotNull VideoRequest request) throws UsuarioNotFoundException {

        usuarioService.buscarPorEmail(request.getPublicadoPor()).subscribe(user -> {
            new VideoUseCase().criarVideo(request, user, videoRepository);
       });


    }

    public Flux<VideoResponse> buscarVideos(AbstractFilter filter) {

        Pageable pageable = filter.getPageableAndSort();

        Query query = new Query().with(pageable);
        query.addCriteria(filter.getCriteria());

        return new VideoUseCase().buscarVideosPaginados(reactiveMongoTemplate, query);

    }
}
