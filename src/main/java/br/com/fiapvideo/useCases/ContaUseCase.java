package br.com.fiapvideo.useCases;

import br.com.fiapvideo.repository.ContaRepository;
import br.com.fiapvideo.useCases.domain.ContaDomain;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

public class ContaUseCase {
    public Mono<ContaDomain> criarConta(ContaRepository contaRepository) {
        return contaRepository.save(new ContaDomain(new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
    }

    public void removerConta(ContaDomain conta, ContaRepository contaRepository) {
        contaRepository.delete(conta).subscribe();
    }
}
