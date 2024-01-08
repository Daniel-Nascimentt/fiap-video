package br.com.fiapvideo.web.controller;

import br.com.fiapvideo.repository.UsuarioRepository;
import br.com.fiapvideo.service.UsuarioService;
import br.com.fiapvideo.useCases.domain.ContaDomain;
import br.com.fiapvideo.useCases.domain.UsuarioDomain;
import br.com.fiapvideo.web.request.UsuarioRequest;
import br.com.fiapvideo.web.response.UsuarioResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = UsuarioController.class)
@AutoConfigureMockMvc
class UsuarioControllerTest {

    private static final String NOME_FAKE_USER_REQUEST = "Nome do usuario request";
    private static final String EMAIL_FAKE_USER_REQUEST = "request@teste.com";
    private static final LocalDate DATA_NASC_FAKE_USER = LocalDate.of(2000, 5, 19);
    private final static String FAKE_EMAIL_USER = "user@teste.com";
    private static final String FAKE_NAME_USER = "Nome do usuario";;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private MongoOperations mongoOperations;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @Autowired
    private WebTestClient webTestClient;

    @DisplayName(value = "Validar endpoint de cadastro de usuário retornando status code 201")
    @Test
    void criarNovoUsuario() throws Exception {
        UsuarioRequest request = getRequestFakeUsuario();
        UsuarioResponse response = new UsuarioResponse();

        when(mongoOperations.count((Query) any(), (Class<?>) any())).thenReturn(0L);
        when(usuarioService.criarNovoUsuario(any())).thenReturn(Mono.just(response));
        when(usuarioRepository.save(any())).thenReturn(Mono.just(getFakeUsuario()));

        webTestClient.post()
                .uri("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(objectMapper.writeValueAsString(request)))
                .exchange()
                .expectStatus().isCreated();
    }

    @DisplayName(value = "Validar endpoint de cadastro de usuário com falha simulando que o e-mail ja existe retornando status code 400.")
    @Test
    void criarNovoUsuarioErroUniqueValue() throws Exception {
        UsuarioRequest request = getRequestFakeUsuario();
        UsuarioResponse response = new UsuarioResponse();

        when(mongoOperations.count((Query) any(), (Class<?>) any())).thenReturn(1L);
        when(usuarioService.criarNovoUsuario(any())).thenReturn(Mono.just(response));
        when(usuarioRepository.save(any())).thenReturn(Mono.just(getFakeUsuario()));

        webTestClient.post()
                .uri("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(objectMapper.writeValueAsString(request)))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @DisplayName(value = "Validar endpoint de atualização de usuário retornando status code 200.")
    @Test
    void atualizarUsuario() throws JsonProcessingException {
        String emailAtual = "email@test.com";
        UsuarioRequest request = getRequestFakeUsuario();
        UsuarioResponse response = new UsuarioResponse();

        when(usuarioService.atualizarUsuario(any(), any())).thenReturn(Mono.just(response));

        webTestClient.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/usuarios")
                        .queryParam("emailAtual", emailAtual)
                        .build())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(objectMapper.writeValueAsString(request)))
                .exchange()
                .expectStatus().isOk();
    }

    @DisplayName(value = "Deve validar endpoint que busca usuário por e-mail retornando status code 200.")
    @Test
    void buscarUsuarioPorEmail() {
        UsuarioDomain usuarioDomain = getFakeUsuario();
        UsuarioResponse usuarioResponse = new UsuarioResponse();

        when(usuarioService.buscarPorEmail(anyString())).thenReturn(Mono.just(usuarioDomain));
        when(usuarioService.converterDomainParaResponse(any())).thenReturn(usuarioResponse);

        webTestClient.get()
                .uri("/usuarios?email={email}", FAKE_EMAIL_USER)
                .exchange()
                .expectStatus().isFound();
    }

    @DisplayName(value = "Deve validar endpoint que remova usuário por e-mail retornando status code 204.")
    @Test
    void removerUsuarioPorEmail() {

        when(usuarioService.removerUsuarioPorEmail(anyString())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/usuarios?email={email}", FAKE_EMAIL_USER)
                .exchange()
                .expectStatus().isNoContent();
    }

    private UsuarioRequest getRequestFakeUsuario() {
        return new UsuarioRequest(
                NOME_FAKE_USER_REQUEST,
                EMAIL_FAKE_USER_REQUEST,
                DATA_NASC_FAKE_USER
        );
    }

    private UsuarioDomain getFakeUsuario() {
        UsuarioDomain usuario = new UsuarioDomain(
                FAKE_NAME_USER,
                FAKE_EMAIL_USER,
                DATA_NASC_FAKE_USER,
                new ContaDomain(new ArrayList<>(), new ArrayList<>(), new ArrayList<>()),
                LocalDateTime.now()
        );
        usuario.setId(UUID.randomUUID().toString());
        return usuario;
    }

}
