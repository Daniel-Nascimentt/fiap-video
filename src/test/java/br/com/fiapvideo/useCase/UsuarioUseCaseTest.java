package br.com.fiapvideo.useCase;

import br.com.fiapvideo.exceptions.UsuarioNotFoundException;
import br.com.fiapvideo.repository.ContaRepository;
import br.com.fiapvideo.repository.UsuarioRepository;
import br.com.fiapvideo.useCases.UsuarioUseCase;
import br.com.fiapvideo.useCases.domain.ContaDomain;
import br.com.fiapvideo.useCases.domain.UsuarioDomain;
import br.com.fiapvideo.web.request.UsuarioRequest;
import br.com.fiapvideo.web.response.UsuarioResponse;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UsuarioUseCaseTest {

    private static final String NOME_FAKE_USER = "Nome do usuario";
    private static final String EMAIL_FAKE_USER = "email@teste.com";
    private static final LocalDate DATA_NASC_FAKE_USER = LocalDate.of(2000, 5, 19);
    private static final String NOME_FAKE_USER_REQUEST = "Nome do usuario request";
    private static final String EMAIL_FAKE_USER_REQUEST = "request@teste.com";

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ContaRepository contaRepository;

    @Test
    @DisplayName(value = "Deve cadastrar um usuario a partir da request.")
    public void criarUsuario(){
        when(usuarioRepository.save(any())).thenReturn(Mono.just(getFakeUsuario()));
        when(contaRepository.save(any())).thenReturn(Mono.just(new ContaDomain(new ArrayList<>(), new ArrayList<>(), new ArrayList<>())));

        Mono<UsuarioResponse> usuarioMono = new UsuarioUseCase().criarNovoUsuario(getRequestFakeUsuario(), usuarioRepository, contaRepository);

        StepVerifier.create(usuarioMono)
                .expectNextMatches(usuario -> {
                    assertNotNull(usuario);
                    assertNotNull(usuario.getConta());
                    assertInstanceOf(UsuarioResponse.class, usuario);
                    verify(usuarioRepository, times(1)).save(any());
                    return true;
                }).verifyComplete();

    }

    @DisplayName(value = "Deve remover um usuário.")
    @Test
    public void removerUsuario(){
        when(usuarioRepository.delete(any())).thenReturn(Mono.empty());
        when(contaRepository.delete(any())).thenReturn(Mono.empty());

        new UsuarioUseCase().removerUsuarioPorEmail(getFakeUsuario(), usuarioRepository, contaRepository);

        verify(usuarioRepository, times(1)).delete(any());
        verify(contaRepository, times(1)).delete(any());
    }

    @DisplayName(value = "Deve buscar um usuário por e-mail.")
    @Test
    public void buscarUsuarioPorEmail(){
        when(usuarioRepository.findByEmail(any())).thenReturn(Mono.just(getFakeUsuario()));

        Mono<UsuarioDomain> usuarioDomainMono = new UsuarioUseCase().buscarPorEmail(EMAIL_FAKE_USER, usuarioRepository);

        StepVerifier.create(usuarioDomainMono)
                .expectNextMatches(usuario -> {
                    assertNotNull(usuario);
                    assertInstanceOf(UsuarioDomain.class, usuario);
                    return true;
                }).verifyComplete();
    }

    @DisplayName(value = "Deve lançar exception ao não encontrar usuário por e-mail.")
    @Test
    public void buscarUsuarioPorEmailException(){
        when(usuarioRepository.findByEmail(any())).thenReturn(Mono.error(new UsuarioNotFoundException()));

        Mono<UsuarioDomain> usuarioDomainMono = new UsuarioUseCase().buscarPorEmail(EMAIL_FAKE_USER, usuarioRepository);

        StepVerifier.create(usuarioDomainMono)
                .expectError(UsuarioNotFoundException.class)
                .verify();
    }

    @DisplayName(value = "Deve atualizar atributos do usuário, menos o id.")
    @Test
    public void atualizarUsuario(){

        UsuarioDomain usuarioDomain = getFakeUsuario();

        when(usuarioRepository.save(any())).thenReturn(Mono.just(usuarioDomain));

        Mono<UsuarioResponse> usuario = new UsuarioUseCase().atualizarUsuario(Mono.just(usuarioDomain), getRequestFakeUsuario(), usuarioRepository);

        StepVerifier.create(usuario)
                .expectNextMatches(user -> {
                    assertNotNull(user);
                    assertInstanceOf(UsuarioResponse.class, user);
                    assertEquals(EMAIL_FAKE_USER_REQUEST, user.getEmail());
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
