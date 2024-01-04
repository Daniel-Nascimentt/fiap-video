package br.com.fiapvideo.useCase;

import br.com.fiapvideo.exceptions.PublicadorNaoCorrespondeException;
import br.com.fiapvideo.exceptions.VideoNotFoundException;
import br.com.fiapvideo.repository.ContaRepository;
import br.com.fiapvideo.repository.UsuarioRepository;
import br.com.fiapvideo.repository.VideoRepository;
import br.com.fiapvideo.useCases.ContaUseCase;
import br.com.fiapvideo.useCases.VideoUseCase;
import br.com.fiapvideo.useCases.domain.ContaDomain;
import br.com.fiapvideo.useCases.domain.PerformanceDomain;
import br.com.fiapvideo.useCases.domain.UsuarioDomain;
import br.com.fiapvideo.useCases.domain.VideoDomain;
import br.com.fiapvideo.web.request.VideoRequest;
import br.com.fiapvideo.web.response.PerformanceResponse;
import br.com.fiapvideo.web.response.RelatorioVideoResponse;
import br.com.fiapvideo.web.response.VideoResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class VideoUseCaseTest {

    private static final String NOME_FAKE_USER = "Nome do usuario";
    private static final String EMAIL_FAKE_USER = "email@teste.com";
    private static final LocalDate DATA_NASC_FAKE_USER = LocalDate.of(2000, 5, 19);
    private static final String VIDEO_FAKE_TITULO = "Fake Video";
    private static final String VIDEO_FAKE_URL = "http://fake.com/video";
    private static final String VIDEO_FAKE_CATEGORIA = "Categoria fake";


    @Mock
    private VideoRepository videoRepository;

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private ReactiveMongoTemplate reactiveMongoTemplate;


    @DisplayName(value = "Deve criar um video.")
    @Test
    public void criarVideo() {

        VideoDomain videoDomain = getVideoFake();

        when(videoRepository.save(any())).thenReturn(Mono.just(videoDomain));
        when(contaRepository.save(any())).thenReturn(Mono.empty());

        Mono<VideoDomain> video = new VideoUseCase().criarVideo(getVideoRequestFake(), getFakeUsuario(), videoRepository, contaRepository);


        StepVerifier.create(video).expectNextMatches(v -> {
            verify(videoRepository, times(1)).save(any());
            assertInstanceOf(VideoDomain.class, v);
            assertNotNull(v.getId());
            return true;
        }).verifyComplete();
    }

    @DisplayName(value = "Deve buscar videos paginadamente.")
    @Test
    public void buscarVideosPaginados(){
        when(reactiveMongoTemplate.find(any(), any())).thenReturn(Flux.just(getVideoFake(), getVideoFake()));
        Flux<VideoResponse> videosResponse = new VideoUseCase().buscarVideosPaginados(reactiveMongoTemplate, new Query());
        verify(reactiveMongoTemplate, times(1)).find(any(), any());
        StepVerifier.create(videosResponse)
                .expectNextCount(2).expectComplete().verify();

    }

    @DisplayName(value = "Deve incrementar a quantidade de views de um vídeo")
    @Test
    public void visualizarVideo(){
        when(reactiveMongoTemplate.updateFirst(any(Query.class), any(Update.class), any(Class.class))).thenReturn(Mono.empty());
        when(contaRepository.save(any())).thenReturn(Mono.empty());

        VideoDomain video = getVideoFake();

        new VideoUseCase().visualizarVideo(getFakeUsuario(), video, contaRepository, reactiveMongoTemplate);
        verify(reactiveMongoTemplate, times(1)).updateFirst(any(Query.class), any(Update.class), any(Class.class));
    }

    @DisplayName(value = "Deve favoritar um vídeo")
    @Test
    public void favoritarVideo(){
        when(reactiveMongoTemplate.updateFirst(any(Query.class), any(Update.class), any(Class.class))).thenReturn(Mono.empty());
        when(contaRepository.save(any())).thenReturn(Mono.empty());

        VideoDomain video = getVideoFake();

        new VideoUseCase().favoritarVideo(getFakeUsuario(), video, contaRepository, reactiveMongoTemplate);
        verify(reactiveMongoTemplate, times(1)).updateFirst(any(Query.class), any(Update.class), any(Class.class));
    }

    @DisplayName(value = "Deve atualizar video se o e-mail fornecido for do publicador do video")
    @Test
    public void atualizarVideoComSucesso(){

        VideoRequest videoRequest = getVideoRequestFake();
        VideoDomain videoDomain = getVideoFake();

        when(videoRepository.findById(anyString())).thenReturn(Mono.just(videoDomain));
        when(videoRepository.save(any())).thenReturn(Mono.just(videoDomain));

        Mono<VideoResponse> videoMono = new VideoUseCase().atualizarVideo(videoRequest, videoDomain.getId(), this.videoRepository);

        StepVerifier.create(videoMono).expectNextMatches(
                video -> {
                    assertInstanceOf(VideoResponse.class, video);
                    assertEquals(videoRequest.getTitulo(), video.getTitulo());
                    return true;
                }
        ).verifyComplete();

    }

    @DisplayName(value = "Deve retornar erro por nao encontrar video.")
    @Test
    public void atualizarVideoExceptionVideoNotFound(){

        VideoRequest videoRequest = getVideoRequestFake();
        VideoDomain videoDomain = getVideoFake();

        when(videoRepository.findById(anyString())).thenReturn(Mono.error(new VideoNotFoundException()));

        Mono<VideoResponse> videoMono = new VideoUseCase().atualizarVideo(videoRequest, videoDomain.getId(), this.videoRepository);

        StepVerifier.create(videoMono).expectError(VideoNotFoundException.class).verify();

    }

    @DisplayName(value = "Deve retornar erro por nao coincidir e-mail de quem publicou e quem esta atualizando.")
    @Test
    public void atualizarVideoPublicadorNaoCorrespondeException(){

        VideoRequest videoRequest = new VideoRequest(VIDEO_FAKE_TITULO, VIDEO_FAKE_CATEGORIA, "email.alterado@teste.com");
        VideoDomain videoDomain = getVideoFake();

        when(videoRepository.findById(anyString())).thenReturn(Mono.just(videoDomain));

        Mono<VideoResponse> videoMono = new VideoUseCase().atualizarVideo(videoRequest, videoDomain.getId(), this.videoRepository);

        StepVerifier.create(videoMono)
                .expectErrorMatches(err ->
                        err instanceof PublicadorNaoCorrespondeException
                                && err.getMessage().equals("E-mail do publicador não corresponde.")).verify();

    }

    @DisplayName(value = "Deve excluir video.")
    @Test
    public void deveExcluirVideo(){

        VideoDomain videoDomain = getVideoFake();

        when(videoRepository.findById(anyString())).thenReturn(Mono.just(videoDomain));
        when(videoRepository.delete(any())).thenReturn(Mono.empty());

        Mono<Void> videoMono = new VideoUseCase().excluirVideo(videoDomain.getId(), this.videoRepository);

        StepVerifier.create(videoMono)
                .expectComplete().verify();

        verify(videoRepository, times(1)).findById(anyString());
        verify(videoRepository, times(1)).delete(any());
    }

    @DisplayName(value = "Deve dar erro ao tentar excluir video.")
    @Test
    public void deveRetornarErroAoTentarExcluirVideo(){

        VideoDomain videoDomain = getVideoFake();

        when(videoRepository.findById(anyString())).thenReturn(Mono.error(new VideoNotFoundException()));

        Mono<Void> videoMono = new VideoUseCase().excluirVideo(videoDomain.getId(), this.videoRepository);


        StepVerifier.create(videoMono)
                .expectErrorMatches(err -> err.getMessage().equals("Vídeo não encontrado!!") && err instanceof VideoNotFoundException).verify();

        verify(videoRepository, times(1)).findById(anyString());
        verify(videoRepository, times(0)).delete(any());
    }

    @DisplayName(value = "Deve exeutar query e retornar relatório de vídeos.")
    @Test
    public void deveRetornarRelatorioEstatistica(){

        when(reactiveMongoTemplate.aggregate(ArgumentMatchers.any(), ArgumentMatchers.anyString(), ArgumentMatchers.any()))
                .thenReturn(Flux.just(new RelatorioVideoResponse()));

        Mono<RelatorioVideoResponse> response = new VideoUseCase().calcularEstatisticasVideos(this.reactiveMongoTemplate);

        StepVerifier.create(response).expectNextMatches(resp -> {
            assertInstanceOf(RelatorioVideoResponse.class, resp);
            return true;
        }).verifyComplete();

    }


    private VideoResponse getVideoResponseFake(){

        VideoDomain video = getVideoFake();

        return new VideoResponse(
                video.getCategoria(),
                video.getUrl(),
                video.getDataPublicacao(),
                new PerformanceResponse(0L, 0L),
                video.getTitulo()
        );
    }


    private VideoRequest getVideoRequestFake() {
        return new VideoRequest(
                VIDEO_FAKE_TITULO,
                VIDEO_FAKE_CATEGORIA,
                EMAIL_FAKE_USER);
    }

    private VideoDomain getVideoFake() {
        VideoDomain video = new VideoDomain(
                VIDEO_FAKE_TITULO,
                VIDEO_FAKE_URL,
                LocalDateTime.now(),
                new PerformanceDomain(0L, 0L),
                VIDEO_FAKE_CATEGORIA,
                getFakeUsuario());

        video.setId(UUID.randomUUID().toString());
        return video;
    }

    private UsuarioDomain getFakeUsuario() {
        UsuarioDomain usuario = new UsuarioDomain(
                NOME_FAKE_USER,
                EMAIL_FAKE_USER,
                DATA_NASC_FAKE_USER,
                new ContaDomain(new ArrayList<>(), new ArrayList<>(), new ArrayList<>()),
                LocalDateTime.now()
        );

        usuario.setId(UUID.randomUUID().toString());
        return usuario;
    }

}
