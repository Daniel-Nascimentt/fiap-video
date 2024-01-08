package br.com.fiapvideo.web.controller;

import br.com.fiapvideo.filters.DynamicFilterVideo;
import br.com.fiapvideo.service.VideoService;
import br.com.fiapvideo.useCases.domain.ContaDomain;
import br.com.fiapvideo.useCases.domain.PerformanceDomain;
import br.com.fiapvideo.useCases.domain.UsuarioDomain;
import br.com.fiapvideo.useCases.domain.VideoDomain;
import br.com.fiapvideo.web.request.EspectVideoRequest;
import br.com.fiapvideo.web.request.VideoRequest;
import br.com.fiapvideo.web.response.RelatorioVideoResponse;
import br.com.fiapvideo.web.response.VideoResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = VideoController.class)
@AutoConfigureMockMvc
public class VideoControllerTest {

    private final static String FAKE_EMAIL_USER = "user@teste.com";
    private static final String FAKE_NAME_USER = "Nome do usuario";;
    private static final LocalDate FAKE_DATA_NASC_USER = LocalDate.of(2000, 5, 19);
    private static final String VIDEO_FAKE_TITULO = "Fake Video";
    private static final String VIDEO_FAKE_URL = "http://fake.com/video";
    private static final String VIDEO_FAKE_CATEGORIA = "Categoria fake";

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VideoService videoService;

    @Autowired
    private WebTestClient webTestClient;


    @DisplayName(value = "Deve validar endpoint que cria video e retornar status code 201.")
    @Test
    void criarVideo() throws Exception {
        VideoRequest request = getVideoRequestFake();
        VideoResponse response = new VideoResponse();

        when(videoService.criarVideo(any())).thenReturn(Mono.just(response));

        webTestClient.post()
                .uri("/videos")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.writeValueAsString(request))
                .exchange()
                .expectStatus().isCreated();
    }

    @DisplayName(value = "Deve validar endpoint que busca videos dinamicamente retornando status code 200.")
    @Test
    void buscarVideosDinamicamente() throws Exception {

        String page = "1";
        String size = "10";

        String sortBy = "titulo";
        String sortOrder = "asc";

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add(page, size);
        parameters.add(sortBy, sortOrder);

        // apenas para simular comportamento do e retorno do service
        Flux<VideoResponse> responseFlux = Flux.empty();
        when(videoService.buscarVideosDinamicamente(any())).thenReturn(responseFlux);

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/videos/buscar")
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .queryParam("sortBy", sortBy)
                        .queryParam("sortOrder", sortOrder)
                        .queryParams(parameters)
                        .build())
                .exchange()
                .expectStatus().isOk();
    }

    @DisplayName(value = "Deve validar endpoint que favoria um video retornando um status code 204.")
    @Test
    void favoritarVideo() throws Exception {
        EspectVideoRequest request = new EspectVideoRequest("idFakeVideo", FAKE_EMAIL_USER);

        when(videoService.favoritarVideo(any())).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/videos/favoritar")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.writeValueAsString(request))
                .exchange()
                .expectStatus().isNoContent();
    }

    @DisplayName(value = "Deve validar endpoint que deleta um video retornando um status code 204.")
    @Test
    void excluirVideo() {
        String videoId = "videoId";

        when(videoService.excluirVideo(any())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/videos/{videoId}", videoId)
                .exchange()
                .expectStatus().isNoContent();
    }

    @DisplayName(value = "Deve validar endpoint que atualiza um video retornando um status code 200.")
    @Test
    void atualizarVideo() throws Exception {
        String videoId = "videoId";
        VideoRequest request = getVideoRequestFake();
        VideoResponse response = new VideoResponse();

        when(videoService.atualizarVideo(any(), any())).thenReturn(Mono.just(response));

        webTestClient.put()
                .uri("/videos/{videoId}", videoId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.writeValueAsString(request))
                .exchange()
                .expectStatus().isOk();
    }

    @DisplayName(value = "Deve validar endpoint que recomenda videos ao usuario com status code 200.")
    @Test
    void recomendarTop5Videos() {

        Flux<VideoResponse> responseFlux = Flux.just();

        // simula retorno para validação apenas do endpoint
        when(videoService.recomendacaoTop5VideosPorCategoriaFavoritada(any(), any())).thenReturn(responseFlux);

        webTestClient.get()
                .uri("/videos/{emailUsuario}", FAKE_EMAIL_USER)
                .exchange()
                .expectStatus().isOk();
    }

    @DisplayName(value = "Deve validar endpoint que extrai relatorio da base dados com status code 200.")
    @Test
    void calcularEstatisticasVideos() {

        RelatorioVideoResponse response = new RelatorioVideoResponse();
        when(videoService.calcularEstatisticasVideos()).thenReturn(Mono.just(response));

        webTestClient.get()
                .uri("/videos/relatorio")
                .exchange()
                .expectStatus().isOk();
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
                getFakeUsuario());

        video.setId(UUID.randomUUID().toString());
        return video;
    }

    private VideoRequest getVideoRequestFake() {
        return new VideoRequest(
                VIDEO_FAKE_TITULO,
                VIDEO_FAKE_CATEGORIA,
                FAKE_EMAIL_USER);
    }

}
