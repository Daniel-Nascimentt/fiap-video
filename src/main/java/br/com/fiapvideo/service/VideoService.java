package br.com.fiapvideo.service;


import br.com.fiapvideo.exceptions.UsuarioNotFoundException;
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

    public Flux<VideoResponse> buscarVideos(int page, int size, String sortBy, String sortOrder, String fieldFilter, String fieldValue) {

        Pageable pageable = construirPaginacaoOrdenada(page, size, sortBy, sortOrder);

        Query query = new Query().with(pageable);

        construirFiltroDeConsulta(fieldFilter, fieldValue, query);

        return new VideoUseCase().buscarVideosPaginados(reactiveMongoTemplate, query);

    }

    private void construirFiltroDeConsulta(String fieldFilter, String fieldValue, Query query) {
        Criteria criteria = new Criteria();

        if(fieldFilter != null && fieldValue != null && !fieldFilter.isEmpty() && !fieldValue.isEmpty()) {
            criteria.and(fieldFilter).is(fieldValue);
            query.addCriteria(criteria);
        }
    }

    private Pageable construirPaginacaoOrdenada(int page, int size, String sortBy, String sortOrder) {
        Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        return PageRequest.of(page, size, Sort.by(direction, sortBy));
    }
}
