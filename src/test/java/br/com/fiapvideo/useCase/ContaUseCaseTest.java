package br.com.fiapvideo.useCase;

import br.com.fiapvideo.repository.ContaRepository;
import br.com.fiapvideo.useCases.ContaUseCase;
import br.com.fiapvideo.useCases.domain.ContaDomain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
public class ContaUseCaseTest {

    @Mock
    private ContaRepository contaRepository;

    @DisplayName(value = "Deve criar uma conta.")
    @Test
    public void criarConta(){
        when(contaRepository.save(any())).thenReturn(getContaFake());

        ContaDomain conta = new ContaUseCase().criarConta(contaRepository);

        assertNotNull(conta);
        assertFalse(conta.getId().isEmpty());
        assertTrue(conta.getFavoritos().isEmpty());
        assertTrue(conta.getVideosAssistidos().isEmpty());
        assertTrue(conta.getVideosPublicados().isEmpty());
    }

    private ContaDomain getContaFake() {
        ContaDomain conta = new ContaDomain();
        conta.setId(UUID.randomUUID().toString());
        return conta;
    }

}
