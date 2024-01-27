package br.com.fiapvideo.web.controller;

import br.com.fiapvideo.constants.ConstantsFiapVideo;
import br.com.fiapvideo.integration.config.MongoDBContainerConfig;
import br.com.fiapvideo.repository.UsuarioRepository;
import br.com.fiapvideo.repository.VideoRepository;
import br.com.fiapvideo.useCases.domain.UsuarioDomain;
import br.com.fiapvideo.web.request.VideoRequest;
import br.com.fiapvideo.web.response.RelatorioVideoResponse;
import br.com.fiapvideo.web.response.VideoResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers(disabledWithoutDocker = true)
@ActiveProfiles("test")
@DirtiesContext
public class VideoControllerIT extends MongoDBContainerConfig {

    private final static String EMAIL_USER = "usuario1@example.com";
    private static final String VIDEO_FAKE_CATEGORIA = "Categoria fake";
    private static final String VIDEO_FAKE_DESCRICAO = "Video Fake descricao";
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    public void setup(){
        mapper.registerModule(new JavaTimeModule());
    }

    @DisplayName(value = "Deve fazer requisção para criar um novo video a partir de um usuario existente no banco de dados.")
    @Test
    public void testEndpointCriarVideo() throws JsonProcessingException {
        String json = webTestClient.post()
                .uri("/videos")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(getVideoRequestFake("Teste integrado criando video")), VideoRequest.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();


        VideoResponse response = mapper.readValue(json, VideoResponse.class);

        assertNotNull(response);
        assertNotNull(response.getId());
        assertNotNull(response.getUrl());
        assertNotNull(response.getCategoria());
        assertNotNull(response.getTitulo());
        assertEquals(0, response.getPerformance().getMarcadoFavorito());
        assertEquals(0, response.getPerformance().getVisualizacoes());

    }

    @DisplayName(value = "Deve fazer requisção para criar um novo video e visualizar incrementando valor de views no banco de dados.")
    @Test
    public void testEndpointVisualizarVideo() throws JsonProcessingException {
        String json = webTestClient.post()
                .uri("/videos")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(getVideoRequestFake("Teste integrado criar e visualizar")), VideoRequest.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        VideoResponse response = mapper.readValue(json, VideoResponse.class);

        assertEquals(0L, response.getPerformance().getVisualizacoes());

        webTestClient.get()
                .uri("/videos/visualizar?videoId={videoId}&emailTelespectador={emailTelespectador}", response.getId(), EMAIL_USER)
                .exchange()
                .expectStatus().isNoContent();

        StepVerifier.create(videoRepository.findById(response.getId())).expectNextMatches(
                videoDomain -> {
                    assertEquals("Teste integrado criar e visualizar", videoDomain.getTitulo());
                    assertTrue(videoDomain.getPerformance().getVisualizacoes() > 0);
                    return true;
                }
        ).verifyComplete();

    }

    @DisplayName(value = "Deve fazer requisção para criar um novo video e favoritar incrementando valor de favoritos no banco de dados.")
    @Test
    public void testEndpointFavoritarVideo() throws JsonProcessingException {
        String json = webTestClient.post()
                .uri("/videos")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(getVideoRequestFake("Teste integrado criar e favoritar")), VideoRequest.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        VideoResponse response = mapper.readValue(json, VideoResponse.class);

        assertEquals(0L, response.getPerformance().getMarcadoFavorito());

        webTestClient.post()
                .uri("/videos/favoritar?videoId={videoId}&emailTelespectador={emailTelespectador}", response.getId(), EMAIL_USER)
                .exchange()
                .expectStatus().isNoContent();

        StepVerifier.create(videoRepository.findById(response.getId())).expectNextMatches(
                videoDomain -> {
                    assertEquals("Teste integrado criar e favoritar", videoDomain.getTitulo());
                    assertTrue(videoDomain.getPerformance().getMarcadoFavorito() > 0);
                    return true;
                }
        ).verifyComplete();

    }


    @DisplayName(value = "Deve fazer requisção para criar um novo video e deletar no banco de dados.")
    @Test
    public void testEndpointRemoverVideo() throws JsonProcessingException {

        String json = webTestClient.post()
                .uri("/videos")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(getVideoRequestFake("Teste integrado criar e deletar")), VideoRequest.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        VideoResponse response = mapper.readValue(json, VideoResponse.class);

       webTestClient.delete()
                .uri("/videos/{videoId}", response.getId())
                .exchange()
                .expectStatus().isNoContent();


       StepVerifier.create(videoRepository.findById(response.getId())).expectComplete().verify();

    }


    @DisplayName(value = "Deve fazer requisção para criar um novo video e atualizar no banco de dados.")
    @Test
    public void testEndpointAtualizarVideo() throws JsonProcessingException {

        String json = webTestClient.post()
                .uri("/videos")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(getVideoRequestFake("Teste integrado criar e atualizar")), VideoRequest.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        VideoResponse response = mapper.readValue(json, VideoResponse.class);

        String jsonAtt = webTestClient.put()
                .uri("/videos/{videoId}", response.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(getVideoRequestFake("Titulo do video atualizado.")), VideoRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        VideoResponse responseAtt = mapper.readValue(jsonAtt, VideoResponse.class);

        assertNotNull(responseAtt);
        assertEquals("Titulo do video atualizado.", responseAtt.getTitulo());
        assertEquals(response.getCategoria(), responseAtt.getCategoria());
        assertEquals(response.getId(), responseAtt.getId());
        assertNotEquals(response.getUrl(), responseAtt.getUrl());

    }

    @DisplayName(value = "Deve fazer requisção para extrair relatorio de videos.")
    @Test
    public void testEndpointCalcularEstatisticasVideos() throws JsonProcessingException {

        String json = webTestClient.get()
                .uri("/videos/relatorio")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        RelatorioVideoResponse response = mapper.readValue(json, RelatorioVideoResponse.class);

        assertNotNull(response);
        assertTrue(response.getTotalVideos() > 0);
        assertTrue(response.getMediaViewsPorVideo() > 0);
        assertTrue(response.getTotalViews() > 0);
        assertTrue(response.getTotalMarcadoFavorito() > 0);

    }


    @DisplayName(value = "Deve fazer requisção para recomendar videos considerando ultimo favorito")
    @Test
    public void testEndpointRecomendarTopVideos() throws JsonProcessingException {

        String json = webTestClient.get()
                .uri("/videos/{emailUsuario}", EMAIL_USER)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        List<VideoResponse> responseList = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, VideoResponse.class));

        UsuarioDomain user = usuarioRepository.findByEmail(EMAIL_USER).block();

        assertNotNull(user);

        String categoriaUltimoVideoFavoritado = user.getConta().getUltimoFavorito().getCategoria();

        responseList.forEach(
                videoResponse -> {
                    assertTrue(videoResponse.getPerformance().getVisualizacoes() > 0);
                    assertTrue(videoResponse.getPerformance().getMarcadoFavorito() > 0);
                    assertEquals(categoriaUltimoVideoFavoritado, videoResponse.getCategoria());
                }
        );

    }


    @DisplayName(value = "Deve fazer requisção para buscar videos filtrando por categoria e titulo.")
    @Test
    public void testEndpointBuscarVideosDinamicamenteCenario1() throws JsonProcessingException {

        String tituloVideoCarga = "Call of Duty Warzone";
        String categoriaVideoCarga = "Jogos";

        String json = webTestClient.get()
                .uri("/videos/buscar?categoria={categoria}&titulo={titulo}", categoriaVideoCarga, tituloVideoCarga)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        List<VideoResponse> responseList = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, VideoResponse.class));

        assertEquals(1, responseList.size());
        assertEquals(categoriaVideoCarga, responseList.get(0).getCategoria());
        assertEquals(tituloVideoCarga, responseList.get(0).getTitulo());

    }


    @DisplayName(value = "Deve fazer requisção para buscar videos filtrando por categoria.")
    @Test
    public void testEndpointBuscarVideosDinamicamenteCenario2() throws JsonProcessingException {

        String categoriaVideoCarga = "Jogos";

        String json = webTestClient.get()
                .uri("/videos/buscar?categoria={categoria}", categoriaVideoCarga)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        List<VideoResponse> responseList = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, VideoResponse.class));

        assertNotNull(responseList);
        assertFalse(responseList.isEmpty());

        responseList.forEach(
                videoResponse -> {
                    assertEquals(categoriaVideoCarga, videoResponse.getCategoria());
                }
        );

    }

    @DisplayName(value = "Deve fazer requisção para buscar videos filtrando por data de publicacao.")
    @Test
    public void testEndpointBuscarVideosDinamicamenteCenario3() throws JsonProcessingException {

        String dataDePublicacaoUmDiaAnteriorHoje = LocalDateTime.now().minusDays(1).format(ConstantsFiapVideo.DATE_TIME_FORMATTER);

        String json = webTestClient.get()
                .uri("/videos/buscar?dataPublicacao={dataPublicacao}", dataDePublicacaoUmDiaAnteriorHoje)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        List<VideoResponse> responseList = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, VideoResponse.class));

        assertFalse(responseList.isEmpty());

        responseList.forEach(videoResponse -> {
            assertTrue(videoResponse.getDataPublicacao().isAfter(LocalDate.parse(dataDePublicacaoUmDiaAnteriorHoje).atTime(0, 0)));
        });

    }


    private VideoRequest getVideoRequestFake(String titulo) {
        return new VideoRequest(
                titulo,
                VIDEO_FAKE_CATEGORIA,
                VIDEO_FAKE_DESCRICAO,
                EMAIL_USER);
    }

}
