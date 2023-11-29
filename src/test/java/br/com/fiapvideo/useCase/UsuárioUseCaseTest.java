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
import org.mockito.junit.jupiter.MockitoExtension;

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
        when(usuarioRepository.save(any())).thenReturn(getFakeUsuario());

        UsuarioResponse usuario = new UsuarioUseCase().criarNovoUsuario(getRequestFakeUsuario(), usuarioRepository, contaRepository);

        assertNotNull(usuario);
        assertInstanceOf(UsuarioResponse.class, usuario);
        verify(usuarioRepository, times(1)).save(any());

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
                new ContaDomain(new ArrayList<>(), new ArrayList<>(), new ArrayList<>()),
                LocalDateTime.now()
        );
    }

}
