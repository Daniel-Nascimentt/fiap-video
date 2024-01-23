package br.com.fiapvideo.web.controller;

import br.com.fiapvideo.integration.config.MongoDBContainerConfig;
import br.com.fiapvideo.web.request.UsuarioRequest;
import br.com.fiapvideo.web.response.UsuarioResponse;
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

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers(disabledWithoutDocker = true)
@ActiveProfiles("test")
@DirtiesContext
public class UsuarioControllerIT extends MongoDBContainerConfig {

    private static final String NOME_FAKE_USER_REQUEST = "Nome do usuario request";
    private static final String EMAIL_FAKE_USER_REQUEST = "usuario-controller@teste.com";
    private static final LocalDate DATA_NASC_FAKE_USER = LocalDate.of(2000, 5, 19);

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    public void setup(){
        mapper.registerModule(new JavaTimeModule());
    }

    @DisplayName(value = "Deve fazer requisção para criar um novo usuário no banco de dados.")
    @Test
    public void testEndpointCriarUsuario() throws JsonProcessingException {
        String json = webTestClient.post()
                .uri("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(getRequestFakeUsuario()), UsuarioRequest.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        UsuarioResponse response = mapper.readValue(json, UsuarioResponse.class);

        assertNotNull(response);
        assertNotNull(response.getConta());
        assertTrue(response.getConta().getFavoritos().isEmpty());
        assertTrue(response.getConta().getVideosAssistidos().isEmpty());
        assertTrue(response.getConta().getVideosPublicados().isEmpty());

    }

    @DisplayName(value = "Deve fazer requisição para criar um usuário e depois apaga-lo.")
    @Test
    public void testEndpointCriarEApagarUsuario() {

        webTestClient.post()
                .uri("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(new UsuarioRequest(
                        NOME_FAKE_USER_REQUEST,
                        "test-delete@email.com",
                        DATA_NASC_FAKE_USER
                )), UsuarioRequest.class)
                .exchange()
                .expectStatus().isCreated();

        webTestClient.delete()
                .uri("/usuarios?email={email}", "test-delete@email.com")
                .exchange()
                .expectStatus().isNoContent();

    }

    @DisplayName(value = "Deve fazer requisição e buscar usuário por e-mail e o usuário deve conter uma conta.")
    @Test
    public void testEndpointBuscarPorEmail() throws IOException {

        String json = webTestClient.get()
                .uri("/usuarios?email={email}", "usuario1@example.com")
                .exchange()
                .expectStatus().isFound()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        UsuarioResponse response = mapper.readValue(json, UsuarioResponse.class);

        assertNotNull(response);
        assertNotNull(response.getConta());
        assertFalse(response.getConta().getFavoritos().isEmpty());
        assertFalse(response.getConta().getVideosAssistidos().isEmpty());
        assertFalse(response.getConta().getVideosPublicados().isEmpty());

    }


    @DisplayName(value = "Deve fazer requisição para criar um usuário e depois atualiza-lo e o retorno deve trazer informações somente sobre o usuario.")
    @Test
    public void testEndpointCriarEAtualizaUsuario() throws JsonProcessingException {

        webTestClient.post()
                .uri("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(new UsuarioRequest(
                        NOME_FAKE_USER_REQUEST,
                        "test-update@email.com",
                        DATA_NASC_FAKE_USER
                )), UsuarioRequest.class)
                .exchange()
                .expectStatus().isCreated();

        String json = webTestClient.put()
                .uri("/usuarios?emailAtual={email}", "test-update@email.com")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(new UsuarioRequest(
                        NOME_FAKE_USER_REQUEST,
                        "test-update-atualizado@email.com",
                        DATA_NASC_FAKE_USER
                )), UsuarioRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        UsuarioResponse response = mapper.readValue(json, UsuarioResponse.class);

        assertNotNull(response);
        assertNull(response.getConta());
        assertEquals("test-update-atualizado@email.com", response.getEmail());

    }


    private UsuarioRequest getRequestFakeUsuario() {
        return new UsuarioRequest(
                NOME_FAKE_USER_REQUEST,
                EMAIL_FAKE_USER_REQUEST,
                DATA_NASC_FAKE_USER
        );
    }
}
