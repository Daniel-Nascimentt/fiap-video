package br.com.fiapvideo.useCases;

import br.com.fiapvideo.repository.ContaRepository;
import br.com.fiapvideo.useCases.domain.ContaDomain;

public class ContaUseCase {
    public ContaDomain criarConta(ContaRepository contaRepository) {
        return contaRepository.save(new ContaDomain());
    }
}
