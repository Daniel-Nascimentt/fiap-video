package br.com.fiapvideo.useCases;

import br.com.fiapvideo.repository.ContaRepository;
import br.com.fiapvideo.useCases.domain.ContaDomain;
import br.com.fiapvideo.useCases.domain.VideoDomain;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

public class ContaUseCase {

    public Mono<ContaDomain> criarConta(ContaRepository contaRepository) {
        return contaRepository.save(new ContaDomain(new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
    }

    public void removerConta(ContaDomain conta, ContaRepository contaRepository) {
         contaRepository.delete(conta).subscribe();
    }

    public void addVideoHistoricoAssistido(ContaDomain conta, VideoDomain videoDomain, ContaRepository contaRepository) {
        conta.addAssistido(videoDomain);
        contaRepository.save(conta).subscribe();
    }

    public void addVideoPublicado(ContaDomain conta, ContaRepository contaRepository, VideoDomain video) {
        conta.addPublicado(video);
        contaRepository.save(conta).subscribe();
    }

    public void addVideoHistoricoFavoritos(ContaDomain conta, VideoDomain videoDomain, ContaRepository contaRepository) {
        conta.addFavorito(videoDomain);
        contaRepository.save(conta).subscribe();
    }

}
