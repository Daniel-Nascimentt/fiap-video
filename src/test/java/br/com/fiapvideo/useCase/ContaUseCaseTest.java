package br.com.fiapvideo.useCase;

import br.com.fiapvideo.repository.ContaRepository;
import br.com.fiapvideo.useCases.ContaUseCase;
import br.com.fiapvideo.useCases.domain.ContaDomain;
import br.com.fiapvideo.useCases.domain.PerformanceDomain;
import br.com.fiapvideo.useCases.domain.UsuarioDomain;
import br.com.fiapvideo.useCases.domain.VideoDomain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContaUseCaseTest {

    private static final String NOME_FAKE_USER = "Nome do usuario";
    private static final String EMAIL_FAKE_USER = "email@teste.com";
    private static final LocalDate DATA_NASC_FAKE_USER = LocalDate.of(2000, 5, 19);
    private static final String VIDEO_FAKE_TITULO = "Fake Video";
    private static final String VIDEO_FAKE_URL = "http://fake.com/video";
    private static final String VIDEO_FAKE_CATEGORIA = "Categoria fake";

    @Mock
    private ContaRepository contaRepository;

    @DisplayName(value = "Deve criar uma conta.")
    @Test
    public void criarConta(){
        when(contaRepository.save(any())).thenReturn(Mono.just(getContaFake()));

        Mono<ContaDomain> contaMono = new ContaUseCase().criarConta(contaRepository);

        StepVerifier.create(contaMono)
                .expectNextMatches(conta -> {
                    assertNotNull(conta);
                    assertFalse(conta.getId().isEmpty());
                    assertTrue(conta.getFavoritos().isEmpty());
                    assertTrue(conta.getVideosAssistidos().isEmpty());
                    assertTrue(conta.getVideosPublicados().isEmpty());
                    return true;
                })
                .verifyComplete();
    }

    @DisplayName(value = "Deve remover uma conta.")
    @Test
    public void removerConta(){

        ContaDomain conta = getContaFake();
        when(contaRepository.delete(conta)).thenReturn(Mono.empty());
        new ContaUseCase().removerConta(conta, contaRepository);
        verify(contaRepository, times(1)).delete(any());
    }

    @DisplayName(value = "Deve adicionar video no historico de assistidos da conta.")
    @Test
    public void addVideoHistoricoAssistido(){

        ContaDomain conta = getContaFake();
        VideoDomain video = getVideoFake();

        when(contaRepository.save(conta)).thenReturn(Mono.empty());
        new ContaUseCase().addVideoHistoricoAssistido(conta, video, contaRepository);

        VideoDomain videoAdicionado = conta.getVideosAssistidos().get(0);

        verify(contaRepository, times(1)).save(any());
        assertEquals(1, conta.getVideosAssistidos().size());
        assertEquals(video.getTitulo(), videoAdicionado.getTitulo());
        assertNotNull(videoAdicionado.getPerformance());

    }


    @DisplayName(value = "Deve adicionar video no historico de publicados da conta.")
    @Test
    public void addVideoPublicado(){

        ContaDomain conta = getContaFake();
        VideoDomain video = getVideoFake();

        when(contaRepository.save(conta)).thenReturn(Mono.empty());
        new ContaUseCase().addVideoPublicado(conta, contaRepository, video);

        VideoDomain videoAdicionado = conta.getVideosPublicados().get(0);

        verify(contaRepository, times(1)).save(any());
        assertEquals(1, conta.getVideosPublicados().size());
        assertEquals(video.getTitulo(), videoAdicionado.getTitulo());
        assertNotNull(videoAdicionado.getPerformance());

    }

    @DisplayName(value = "Deve adicionar video a lista de favoritos da conta.")
    @Test
    public void addVideoHistoricoFavoritos(){

        ContaDomain conta = getContaFake();
        VideoDomain video = getVideoFake();

        when(contaRepository.save(conta)).thenReturn(Mono.empty());
        new ContaUseCase().addVideoHistoricoFavoritos(conta, video, contaRepository);

        VideoDomain videoAdicionado = conta.getFavoritos().get(0);

        verify(contaRepository, times(1)).save(any());
        assertEquals(1, conta.getFavoritos().size());
        assertEquals(video.getTitulo(), videoAdicionado.getTitulo());
        assertNotNull(videoAdicionado.getPerformance());

    }

    private ContaDomain getContaFake() {
        ContaDomain conta = new ContaDomain(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        conta.setId(UUID.randomUUID().toString());
        return conta;
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
        return new UsuarioDomain(
                NOME_FAKE_USER,
                EMAIL_FAKE_USER,
                DATA_NASC_FAKE_USER,
                new ContaDomain(new ArrayList<>(), new ArrayList<>(), new ArrayList<>()),
                LocalDateTime.now()
        );
    }
}
