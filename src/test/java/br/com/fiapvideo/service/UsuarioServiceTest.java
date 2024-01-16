package br.com.fiapvideo.service;

import br.com.fiapvideo.exceptions.UsuarioNotFoundException;
import br.com.fiapvideo.repository.ContaRepository;
import br.com.fiapvideo.repository.UsuarioRepository;
import br.com.fiapvideo.useCases.domain.ContaDomain;
import br.com.fiapvideo.useCases.domain.UsuarioDomain;
import br.com.fiapvideo.web.request.UsuarioRequest;
import br.com.fiapvideo.web.response.ContaResponse;
import br.com.fiapvideo.web.response.UsuarioResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    private final static String FAKE_EMAIL_USER = "user@teste.com";
    private static final String FAKE_NAME_USER = "Nome do usuario";
    private static final LocalDate FAKE_DATA_NASC_USER = LocalDate.of(2000, 5, 19);

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ContaRepository contaRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @DisplayName(value = "Cria novo usuario a partir do use case.")
    @Test
    public void criarNovoUsuario() {

        UsuarioRequest request = new UsuarioRequest();

        when(usuarioRepository.save(any(UsuarioDomain.class))).thenReturn(Mono.just(getFakeUsuario()));
        when(contaRepository.save(any(ContaDomain.class))).thenReturn(getFakeConta());

        Mono<UsuarioResponse> response = usuarioService.criarNovoUsuario(request);

        StepVerifier.create(response)
                .expectNextMatches(resp -> {
                        assertNotNull(resp);
                        assertInstanceOf(UsuarioResponse.class, resp);
                        return true;
                })
                .verifyComplete();
    }


    @DisplayName(value = "Busca usuário por e-mail e o encontra.")
    @Test
    public void BuscarPorEmailUsuarioEncontrado() {
        when(usuarioRepository.findByEmail(FAKE_EMAIL_USER)).thenReturn(Mono.just((new UsuarioDomain())));

        Mono<UsuarioDomain> response = usuarioService.buscarPorEmail(FAKE_EMAIL_USER);

        StepVerifier.create(response)
                .expectNextMatches(usuario -> {
                    assertNotNull(usuario);
                    assertInstanceOf(UsuarioDomain.class, usuario);
                    verify(this.usuarioRepository, times(1)).findByEmail(anyString());
                    return true;
                })
                .verifyComplete();
    }

    @DisplayName(value = "Busca usuário por e-mail e não o encontra.")
    @Test
    public void tuscarPorEmailUsuarioNaoEncontrado() {

        when(usuarioRepository.findByEmail(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(usuarioService.buscarPorEmail(FAKE_EMAIL_USER))
                .expectError(UsuarioNotFoundException.class)
                .verify();
    }

    @DisplayName(value = "Converte usuário domain para usuário response.")
    @Test
    public void converterDomainParaResponse() {
        UsuarioDomain usuarioDomain = getFakeUsuario();

        UsuarioResponse usuarioResponse = usuarioService.converterDomainParaResponse(usuarioDomain);

        assertEquals(usuarioDomain.getNome(), usuarioResponse.getNome());
        assertEquals(usuarioDomain.getEmail(), usuarioResponse.getEmail());
        assertEquals(usuarioDomain.getDataNascimento(), usuarioResponse.getDataNascimento());
        assertInstanceOf(ContaResponse.class, usuarioResponse.getConta());
    }

    @DisplayName(value = "Deve atualizar atributos do usuário.")
    @Test
    public void atualizarUsuario() {

        UsuarioRequest request = new UsuarioRequest();
        request.setEmail("email-alterado@teste.com");

        UsuarioDomain usuarioDomain = getFakeUsuario();
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Mono.just(usuarioDomain));
        when(usuarioRepository.save(any(UsuarioDomain.class))).thenReturn(Mono.just(usuarioDomain));

        Mono<UsuarioResponse> response = usuarioService.atualizarUsuario(FAKE_EMAIL_USER, request);

        StepVerifier.create(response)
                .expectNextMatches(resp -> {
                    assertEquals(request.getEmail(), resp.getEmail());
                    verify(usuarioRepository, times(1)).findByEmail(anyString());
                    verify(usuarioRepository, times(1)).save(any());
                    return true;
                })
                .verifyComplete();
    }

    @DisplayName(value = "Deve atualizar atributos do usuário.")
    @Test
    public void removerUsuarioPorEmail() {

        when(usuarioRepository.findByEmail(anyString())).thenReturn(Mono.just(getFakeUsuario()));
        when(usuarioRepository.delete(any(UsuarioDomain.class))).thenReturn(Mono.empty());
        when(contaRepository.delete(any(ContaDomain.class))).thenReturn(Mono.empty());

        StepVerifier.create(usuarioService.removerUsuarioPorEmail(FAKE_EMAIL_USER))
                .verifyComplete();

        verify(usuarioRepository, times(1)).findByEmail(anyString());
        verify(usuarioRepository, times(1)).delete(any());
        verify(contaRepository, times(1)).delete(any());
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

    private Mono<ContaDomain> getFakeConta() {

        ContaDomain contaDomain = new ContaDomain(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        return Mono.just(contaDomain);
    }

}