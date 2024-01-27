package br.com.fiapvideo.service;

import br.com.fiapvideo.constants.ConstantsFiapVideo;
import br.com.fiapvideo.filters.DynamicFilterVideo;
import br.com.fiapvideo.filters.RecomendacaoFilterVideo;
import br.com.fiapvideo.integration.config.MongoDBContainerConfig;
import br.com.fiapvideo.repository.VideoRepository;
import br.com.fiapvideo.web.request.EspectVideoRequest;
import br.com.fiapvideo.web.request.VideoRequest;
import br.com.fiapvideo.web.response.RelatorioVideoResponse;
import br.com.fiapvideo.web.response.VideoResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers(disabledWithoutDocker = true)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class VideoServiceIT extends MongoDBContainerConfig {

    private final static String EMAIL_USER = "usuario1@example.com";
    private static final String EMAIL_USER_2 = "usuario2@example.com";
    private static final String VIDEO_FAKE_TITULO = "Fake Video";
    private static final String VIDEO_FAKE_CATEGORIA = "Fakes";
    private static final String CATEGORIA_TEST_VALIDATION = "PROGRAMACAO";
    private static final long QTD_VIDEOS_CARGA_INICIAL_PROGRAMACAO = 3L;
    private static final String VIDEO_FAKE_DESCRICAO = "Video Fake descricao";

    @Autowired
    private VideoService videoService;

    @Autowired
    private VideoRepository videoRepository;

    @DisplayName(value = "Deve criar um novo video no banco de dados.")
    @Test
    public void criarVideo(){
        StepVerifier.create(videoService.criarVideo(getVideoRequestFake())).expectNextMatches(
                videoResponse -> {
                    assertNotNull(videoResponse);
                    assertInstanceOf(VideoResponse.class, videoResponse);
                    assertNotNull(videoResponse.getId());
                    return true;
                }
        ).verifyComplete();
    }

    @DisplayName(value = "Deve calcular estatisticas dos videos no banco de dados.")
    @Test
    public void calcularEstatisticasVideos(){
        StepVerifier.create(videoService.calcularEstatisticasVideos()).expectNextMatches(
                relatorio -> {
                    assertNotNull(relatorio);
                    assertInstanceOf(RelatorioVideoResponse.class, relatorio);
                    assertTrue(relatorio.getTotalVideos() > 0);
                    assertTrue(relatorio.getMediaViewsPorVideo() > 0);
                    assertTrue(relatorio.getTotalViews() > 0);
                    assertTrue(relatorio.getTotalMarcadoFavorito() > 0);
                    return true;
                }
        ).verifyComplete();
    }

    @DisplayName(value = "Deve recomendar 2 videos com base no ultimo favorito do usuario.")
    @Test
    public void recomendacaoVideos(){
        StepVerifier.create(videoService.recomendacaoTopVideosPorCategoriaFavoritada(EMAIL_USER, new RecomendacaoFilterVideo()))
                .expectNextMatches(video -> {
                    assertNotNull(video);
                    assertTrue(video.getPerformance().getVisualizacoes() > 0);
                    assertEquals(CATEGORIA_TEST_VALIDATION, video.getCategoria().toUpperCase());
                    return true;
                }).expectNextMatches(video -> {
                    assertNotNull(video);
                    assertTrue(video.getPerformance().getVisualizacoes() > 0);
                    assertEquals(CATEGORIA_TEST_VALIDATION, video.getCategoria().toUpperCase());
                    return true;
               }).verifyComplete();


    }

    @DisplayName(value = "Deve criar um video e ao visualizar, deve incrementar 1 a quantidade de views.")
    @Test
    public void criarVisualizarEConsultarIncrementoDeView(){

        VideoResponse video = videoService.criarVideo(getVideoRequestFake()).block();

        assertNotNull(video);
        assertEquals(0L, video.getPerformance().getVisualizacoes());

        videoService.visualizarVideo(getEspectVideoRequest(video.getId())).block();

        StepVerifier.create(videoRepository.findById(video.getId())).expectNextMatches(videoAtt -> {
            assertNotNull(videoAtt);
            assertEquals(1L, videoAtt.getPerformance().getVisualizacoes());
            return true;
        }).verifyComplete();


    }

    @DisplayName(value = "Deve criar um video e ao favoritar, deve incrementar 1 a quantidade de favoritos.")
    @Test
    public void criarFavoritarEConsultarIncrementoDeView(){

        VideoResponse video = videoService.criarVideo(new VideoRequest(
                VIDEO_FAKE_TITULO,
                VIDEO_FAKE_CATEGORIA,
                VIDEO_FAKE_DESCRICAO,
                EMAIL_USER_2)).block();

        assertNotNull(video);
        assertEquals(0L, video.getPerformance().getMarcadoFavorito());

        videoService.favoritarVideo(new EspectVideoRequest(video.getId(), EMAIL_USER_2)).block();

        StepVerifier.create(videoRepository.findById(video.getId())).expectNextMatches(videoAtt -> {
            assertNotNull(videoAtt);
            assertEquals(1L, videoAtt.getPerformance().getMarcadoFavorito());
            return true;
        }).verifyComplete();

    }


    @DisplayName(value = "Deve criar e excluir um video do banco de dados a partir do id.")
    @Test
    public void criarEExcluirVideo(){

        VideoResponse video = videoService.criarVideo(getVideoRequestFake()).block();

        assertNotNull(video);
        assertNotNull(video.getId());

        StepVerifier.create(videoService.excluirVideo(video.getId())).expectComplete().verify();
    }


    @DisplayName(value = "Deve criar e atualizar atributos de um video.")
    @Test
    public void criarEAtualizarUmVideo(){

        VideoResponse video = videoService.criarVideo(new VideoRequest(
                VIDEO_FAKE_TITULO,
                VIDEO_FAKE_CATEGORIA,
                VIDEO_FAKE_DESCRICAO,
                EMAIL_USER_2
        )).block();

        assertNotNull(video);
        assertNotNull(video.getId());
        assertEquals(VIDEO_FAKE_TITULO, video.getTitulo());
        assertEquals(VIDEO_FAKE_CATEGORIA, video.getCategoria());

        Mono<VideoResponse> videoAtt = videoService.atualizarVideo(
                new VideoRequest(
                        "Titulo modificado",
                        "Categoria modificada",
                        VIDEO_FAKE_DESCRICAO,
                        EMAIL_USER_2), video.getId()
        );

        StepVerifier.create(videoAtt).expectNextMatches(v -> {
            assertNotNull(v);
            assertEquals("Titulo modificado", v.getTitulo());
            assertEquals("Categoria modificada", v.getCategoria());
            assertEquals(video.getId(), v.getId());
            assertInstanceOf(VideoResponse.class, v);
            return true;
        }).verifyComplete();

    }

    @DisplayName(value = "Busca dinamica de videos utilizando filtros distintos.")
    @Test
    public void buscarVideosDinamicamente(){

        Map<String, String> mapFilterTituloAndCategoria = Map.of(
                "titulo", "Call of Duty BO3",
                "categoria", "Jogos");

        Flux<VideoResponse> resultTituloECategoria = videoService.buscarVideosDinamicamente(new DynamicFilterVideo(
                Integer.parseInt(ConstantsFiapVideo.DEFAULT_VALUE_PAGE),
                Integer.parseInt(ConstantsFiapVideo.DEFAULT_VALUE_SIZE),
                ConstantsFiapVideo.DEFAULT_VALUE_SORTBY,
                ConstantsFiapVideo.DEFAULT_VALUE_SORTORDER,
                mapFilterTituloAndCategoria
        ));

        StepVerifier.create(resultTituloECategoria).expectNextMatches(videoResponse -> {
            assertEquals("Call of Duty BO3", videoResponse.getTitulo());
            assertEquals("Jogos", videoResponse.getCategoria());
            return true;
        }).verifyComplete();

        String data = LocalDate.now().format(ConstantsFiapVideo.DATE_TIME_FORMATTER);

        Map<String, String> mapFilterCategoriaEDataPublicacao = Map.of(
                "categoria", "programacao",
                "dataPublicacao", data);

        Flux<VideoResponse> resultCategoriaEDataPublicacao = videoService.buscarVideosDinamicamente(new DynamicFilterVideo(
                Integer.parseInt(ConstantsFiapVideo.DEFAULT_VALUE_PAGE),
                Integer.parseInt(ConstantsFiapVideo.DEFAULT_VALUE_SIZE),
                ConstantsFiapVideo.DEFAULT_VALUE_SORTBY,
                ConstantsFiapVideo.DEFAULT_VALUE_SORTORDER,
                mapFilterCategoriaEDataPublicacao
        ));

        StepVerifier.create(resultCategoriaEDataPublicacao).expectNextCount(QTD_VIDEOS_CARGA_INICIAL_PROGRAMACAO).verifyComplete();
    }

    private EspectVideoRequest getEspectVideoRequest(String idVideo){
        return new EspectVideoRequest(
            idVideo, EMAIL_USER
        );
    }

    private VideoRequest getVideoRequestFake() {
        return new VideoRequest(
                VIDEO_FAKE_TITULO,
                VIDEO_FAKE_CATEGORIA,
                VIDEO_FAKE_DESCRICAO,
                EMAIL_USER);
    }


}
