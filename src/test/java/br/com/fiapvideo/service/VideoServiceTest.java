package br.com.fiapvideo.service;

import br.com.fiapvideo.constants.ConstantsFiapVideo;
import br.com.fiapvideo.exceptions.UsuarioNotFoundException;
import br.com.fiapvideo.exceptions.VideoNotFoundException;
import br.com.fiapvideo.filters.DynamicFilterVideo;
import br.com.fiapvideo.filters.RecomendacaoFilterVideo;
import br.com.fiapvideo.repository.ContaRepository;
import br.com.fiapvideo.repository.VideoRepository;
import br.com.fiapvideo.useCases.domain.ContaDomain;
import br.com.fiapvideo.useCases.domain.PerformanceDomain;
import br.com.fiapvideo.useCases.domain.UsuarioDomain;
import br.com.fiapvideo.useCases.domain.VideoDomain;
import br.com.fiapvideo.web.request.EspectVideoRequest;
import br.com.fiapvideo.web.request.VideoRequest;
import br.com.fiapvideo.web.response.RelatorioVideoResponse;
import br.com.fiapvideo.web.response.VideoResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VideoServiceTest {

    private final static String FAKE_EMAIL_USER = "user@teste.com";
    private static final String FAKE_NAME_USER = "Nome do usuario";;
    private static final LocalDate FAKE_DATA_NASC_USER = LocalDate.of(2000, 5, 19);
    private static final String VIDEO_FAKE_TITULO = "Fake Video";
    private static final String VIDEO_FAKE_URL = "http://fake.com/video";
    private static final String VIDEO_FAKE_CATEGORIA = "Categoria fake";
    private static final String VIDEO_FAKE_DESCRICAO = "Descricao fake";


    @Mock
    private VideoRepository videoRepository;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @InjectMocks
    private VideoService videoService;


    @DisplayName(value = "Deve criar video a partir de um usuário valido.")
    @Test
    public void criarVideo() throws UsuarioNotFoundException {

        when(usuarioService.buscarPorEmail(anyString())).thenReturn(Mono.just(getFakeUsuario()));
        when(videoRepository.save(any(VideoDomain.class))).thenReturn(Mono.just(getVideoFake()));
        when(contaRepository.save(any())).thenReturn(Mono.empty());

        Mono<VideoResponse> video = videoService.criarVideo(getVideoRequestFake());

        StepVerifier.create(video).expectNextMatches(v -> {
            assertInstanceOf(VideoResponse.class, v);
            verify(usuarioService, times(1)).buscarPorEmail(anyString());
            verify(videoRepository, times(1)).save(any());
            return true;
        }).verifyComplete();
    }

    @DisplayName(value = "Deve buscar video dinamicamente sem parametros de busca.")
    @ParameterizedTest
    @ValueSource(strings = {"asc", "desc"})
    public void buscarVideosDinamicamenteSemParametrosDeBusca(String sortOrder) {

        when(reactiveMongoTemplate.find(any(), any())).thenReturn(Flux.just(getVideoFake()));

        Flux<VideoResponse> response = videoService.buscarVideosDinamicamente(new DynamicFilterVideo(
                Integer.parseInt(ConstantsFiapVideo.DEFAULT_VALUE_PAGE),
                Integer.parseInt(ConstantsFiapVideo.DEFAULT_VALUE_SIZE),
                ConstantsFiapVideo.DEFAULT_VALUE_SORTBY,
                sortOrder,
                new HashMap<>()
        ));

        StepVerifier.create(response).expectNextMatches(v -> {
            verify(reactiveMongoTemplate, times(1)).find(any(), any());
            assertFalse(v.getId().isEmpty());
            assertNotNull(v.getId());
            return true;
        }).verifyComplete();

    }

    @DisplayName(value = "Deve buscar video dinamicamente com parametros de busca.")
    @ParameterizedTest
    @MethodSource("getFilters")
    public void buscarVideosDinamicamenteComParametrosDeBusca(HashMap<String, String> filters) {

        when(reactiveMongoTemplate.find(any(), any())).thenReturn(Flux.just(getVideoFake()));

        Flux<VideoResponse> response = videoService.buscarVideosDinamicamente(new DynamicFilterVideo(
                Integer.parseInt(ConstantsFiapVideo.DEFAULT_VALUE_PAGE),
                Integer.parseInt(ConstantsFiapVideo.DEFAULT_VALUE_SIZE),
                ConstantsFiapVideo.DEFAULT_VALUE_SORTBY,
                ConstantsFiapVideo.DEFAULT_VALUE_SORTORDER,
                filters
        ));

        StepVerifier.create(response).expectNextMatches(v -> {
            verify(reactiveMongoTemplate, times(1)).find(any(), any());
            assertFalse(v.getId().isEmpty());
            assertNotNull(v.getId());
            return true;
        }).verifyComplete();

    }

    @DisplayName(value = "Deve validar chamada ao incrementar view.")
    @Test
    public void visualizarVideo() throws UsuarioNotFoundException, VideoNotFoundException {
        EspectVideoRequest request = new EspectVideoRequest(UUID.randomUUID().toString(), FAKE_EMAIL_USER);

        when(usuarioService.buscarPorEmail(anyString())).thenReturn(Mono.just(getFakeUsuario()));
        when(videoRepository.findById(anyString())).thenReturn(Mono.just(getVideoFake()));
        when(contaRepository.save(any())).thenReturn(Mono.empty());
        when(reactiveMongoTemplate.updateFirst((Query) any(), (Update) any(), (Class<?>) any())).thenReturn(Mono.empty());

        Mono<Void> response = videoService.visualizarVideo(request);

        StepVerifier.create(response)
                .expectComplete()
                .verify();


        verify(reactiveMongoTemplate, times(1)).updateFirst((Query) any(), (Update) any(), (Class<?>) any());
        verify(contaRepository, times(1)).save(any());

    }


    @DisplayName(value = "Deve validar chamada ao incrementar favoritado.")
    @Test
    public void favoritarVideo() {
        EspectVideoRequest request = new EspectVideoRequest(UUID.randomUUID().toString(), FAKE_EMAIL_USER);

        when(usuarioService.buscarPorEmail(anyString())).thenReturn(Mono.just(getFakeUsuario()));
        when(videoRepository.findById(anyString())).thenReturn(Mono.just(getVideoFake()));
        when(contaRepository.save(any())).thenReturn(Mono.empty());
        when(reactiveMongoTemplate.updateFirst((Query) any(), (Update) any(), (Class<?>) any())).thenReturn(Mono.empty());

        Mono<Void> response = videoService.favoritarVideo(request);

        StepVerifier.create(response)
                .expectComplete()
                .verify();

        verify(reactiveMongoTemplate, times(1)).updateFirst((Query) any(), (Update) any(), (Class<?>) any());
        verify(contaRepository, times(1)).save(any());
    }

    @DisplayName(value = "Deve retornar objeto de response com relatório de estatistica dos videos.")
    @Test
    public void calcularEstatisticasVideos (){

        when(reactiveMongoTemplate.aggregate(ArgumentMatchers.any(), ArgumentMatchers.anyString(), ArgumentMatchers.any()))
                .thenReturn(Flux.just(new RelatorioVideoResponse()));

        Mono<RelatorioVideoResponse> response = videoService.calcularEstatisticasVideos();

        StepVerifier.create(response)
                .expectNextMatches(r -> {
                    assertInstanceOf(RelatorioVideoResponse.class, r);
                    return true;
                }).verifyComplete();

    }

    @DisplayName(value = "Deve chamar caso de uso responsavel por excluir um video")
    @Test
    public void excluirVideo(){

        when(videoRepository.findById(anyString())).thenReturn(Mono.just(getVideoFake()));
        when(videoRepository.delete(any())).thenReturn(Mono.empty());

        Mono<Void> voidMono = videoService.excluirVideo(UUID.randomUUID().toString());

        StepVerifier.create(voidMono)
                .expectComplete()
                .verify();

        verify(videoRepository, times(1)).delete(any());
    }


    @DisplayName(value = "Deve chamar caso de uso para atualizar video.")
    @Test
    public void atualizarVideo(){

        VideoRequest videoRequest = new VideoRequest( "titulo modificado",
                VIDEO_FAKE_CATEGORIA,
                VIDEO_FAKE_DESCRICAO,
                FAKE_EMAIL_USER);

        VideoDomain videoDomain = getVideoFake();

        when(videoRepository.findById(anyString())).thenReturn(Mono.just(videoDomain));
        when(videoRepository.save(any())).thenReturn(Mono.just(videoDomain));

        Mono<VideoResponse> video = videoService.atualizarVideo(videoRequest, UUID.randomUUID().toString());

        StepVerifier.create(video)
                .expectNextMatches(v -> {
                    assertInstanceOf(VideoResponse.class, v);
                    assertEquals(videoRequest.getTitulo(), v.getTitulo());
                    return true;
                }).verifyComplete();

        verify(videoRepository, times(1)).save(any());

    }

    @DisplayName(value = "Deve recomendar top 5 videos com mais views baseado no ultimo favorito do usuário.")
    @Test
    public void sistemaRecomendacao(){

        UsuarioDomain usuarioDomain = getFakeUsuario();
        ContaDomain contaDomain = getContaFake();

        usuarioDomain.setConta(contaDomain);


        when(usuarioService.buscarPorEmail(anyString())).thenReturn(Mono.just(usuarioDomain));
        when(reactiveMongoTemplate.find(any(), any())).thenReturn(Flux.just(getVideoFake(), getVideoFake()));

        Flux<VideoResponse> videoResponseFlux = videoService.recomendacaoTopVideosPorCategoriaFavoritada(FAKE_EMAIL_USER, new RecomendacaoFilterVideo());

        StepVerifier.create(videoResponseFlux)
                .expectNextCount(2).verifyComplete();

        verify(reactiveMongoTemplate, times(1)).find(any(), any());
    }

    private ContaDomain getContaFake() {
        ContaDomain conta = new ContaDomain(new ArrayList<>(), new ArrayList<>(), List.of(getVideoFake()));
        conta.setId(UUID.randomUUID().toString());
        return conta;
    }

    private UsuarioDomain getFakeUsuario() {
        UsuarioDomain usuario = new UsuarioDomain(
                FAKE_NAME_USER,
                FAKE_EMAIL_USER,
                FAKE_DATA_NASC_USER,
                new ContaDomain(new ArrayList<>(), new ArrayList<>(), new ArrayList<>()),
                LocalDateTime.now()
        );

        usuario.setId(UUID.randomUUID().toString());
        return usuario;
    }

    private VideoDomain getVideoFake() {
        VideoDomain video = new VideoDomain(
                VIDEO_FAKE_TITULO,
                VIDEO_FAKE_URL,
                LocalDateTime.now(),
                new PerformanceDomain(0L, 0L),
                VIDEO_FAKE_CATEGORIA,
                VIDEO_FAKE_DESCRICAO,
                getFakeUsuario());

        video.setId(UUID.randomUUID().toString());
        return video;
    }

    private VideoRequest getVideoRequestFake() {
        return new VideoRequest(
                VIDEO_FAKE_TITULO,
                VIDEO_FAKE_CATEGORIA,
                VIDEO_FAKE_DESCRICAO,
                FAKE_EMAIL_USER);
    }


   private static Stream<HashMap<String, String>> getFilters() {
        return Stream.of(
                new HashMap<>() {{
                    put("titulo", "Meu Título");
                    put("categoria", "Minha Categoria");
                    put("dataPublicacao", "2024-01-01");
                }}
        );
    }

}