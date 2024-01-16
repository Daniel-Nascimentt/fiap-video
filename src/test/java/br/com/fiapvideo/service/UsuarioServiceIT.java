package br.com.fiapvideo.service;

import br.com.fiapvideo.integration.config.MongoDBContainerConfig;
import br.com.fiapvideo.useCases.domain.ContaDomain;
import br.com.fiapvideo.useCases.domain.UsuarioDomain;
import br.com.fiapvideo.web.request.UsuarioRequest;
import br.com.fiapvideo.web.response.ContaResponse;
import br.com.fiapvideo.web.response.UsuarioResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers(disabledWithoutDocker = true)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UsuarioServiceIT extends MongoDBContainerConfig {

    private static final String NOME_FAKE_USER_REQUEST = "Nome do usuario request";
    private static final String EMAIL_FAKE_USER_REQUEST = "request@teste.com";
    private static final LocalDate DATA_NASC_FAKE_USER = LocalDate.of(2000, 5, 19);


    @LocalServerPort
    int port;

    @Autowired
    private UsuarioService usuarioService;


    @DisplayName(value = "Deve Registrar um novo usuário no banco de dados.")
    @Test
    public void criarNovoUsuario(){
        StepVerifier.create(usuarioService.criarNovoUsuario(getRequestFakeUsuario())).expectNextMatches(
                usuario -> {
                    assertInstanceOf(UsuarioResponse.class, usuario);
                    assertNotNull(usuario.getConta());
                    assertInstanceOf(ContaResponse.class, usuario.getConta());
                    assertEquals(NOME_FAKE_USER_REQUEST, usuario.getNome());
                    return true;
                }
        ).verifyComplete();
    }

    @DisplayName(value = "Deve buscar um usuário no banco de dados por e-mail.")
    @Test
    public void buscarUsuarioPorEmail(){

        StepVerifier.create(usuarioService.buscarPorEmail("usuario1@example.com")).expectNextMatches(
                usuario -> {
                    assertNotNull(usuario);
                    assertInstanceOf(UsuarioDomain.class, usuario);
                    assertNotNull(usuario.getConta());
                    assertInstanceOf(ContaDomain.class, usuario.getConta());
                    return true;
        }).verifyComplete();

    }

    @DisplayName(value = "Deve remover um usuário no banco de dados a partir do e-mail.")
    @Test
    public void removerUsuarioPorEmail(){
        StepVerifier.create(usuarioService.removerUsuarioPorEmail("usuario2@example.com"))
                .expectComplete()
                .verify();
    }

    @DisplayName(value = "Deve atualizar dados do usuário")
    @Test
    public void atualizarUsuario(){

        StepVerifier.create(usuarioService.atualizarUsuario("usuario1@example.com", getRequestFakeUsuario()))
                .expectNextMatches(usuarioAtt -> {
                    assertNotNull(usuarioAtt);
                    assertInstanceOf(UsuarioResponse.class, usuarioAtt);
                    assertEquals(EMAIL_FAKE_USER_REQUEST, usuarioAtt.getEmail());
                    return true;
                }).verifyComplete();

    }

    private UsuarioRequest getRequestFakeUsuario() {
        return new UsuarioRequest(
                NOME_FAKE_USER_REQUEST,
                EMAIL_FAKE_USER_REQUEST,
                DATA_NASC_FAKE_USER
        );
    }

}
