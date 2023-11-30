package br.com.fiapvideo.useCase;

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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UsuárioUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ContaRepository contaRepository;

    @Test
    @DisplayName(value = "Deve cadastrar um usuário a partir da request.")
    public void criarUsuario(){
        when(usuarioRepository.save(any())).thenReturn(Mono.just(getFakeUsuario()));
        when(contaRepository.save(any())).thenReturn(Mono.just(new ContaDomain(new ArrayList<>(), new ArrayList<>(), new ArrayList<>())));

        Mono<ResponseEntity<UsuarioResponse>> usuarioMono = new UsuarioUseCase().criarNovoUsuario(getRequestFakeUsuario(), usuarioRepository, contaRepository);

        StepVerifier.create(usuarioMono)
                .expectNextMatches(usuario -> {
                    assertNotNull(usuario.getBody());
                    assertInstanceOf(UsuarioResponse.class, usuario.getBody());
                    verify(usuarioRepository, times(1)).save(any());
                    return true;
                }).verifyComplete();

    }

    private UsuarioRequest getRequestFakeUsuario() {
        return new UsuarioRequest(
                "Nome do usuário",
                "email@teste.com",
                LocalDate.of(2000, 5, 19)
        );
    }

    private UsuarioDomain getFakeUsuario() {
        return new UsuarioDomain(
                "Nome do usuário",
                "email@teste.com",
                LocalDate.of(2000, 5, 19),
                getContaFake(),
                LocalDateTime.now()
        );
    }

    private ContaDomain getContaFake() {
        return new ContaDomain(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

}
