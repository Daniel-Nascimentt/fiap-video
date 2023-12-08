package br.com.fiapvideo.useCases;

import br.com.fiapvideo.repository.VideoRepository;
import br.com.fiapvideo.useCases.domain.CategoriaDomain;
import br.com.fiapvideo.useCases.domain.PerformanceDomain;
import br.com.fiapvideo.useCases.domain.UsuarioDomain;
import br.com.fiapvideo.useCases.domain.VideoDomain;
import br.com.fiapvideo.web.request.VideoRequest;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class VideoUseCase {

    public void criarVideo(@NotNull VideoRequest request, @NotNull UsuarioDomain usuario, @NotNull VideoRepository videoRepository){

        VideoDomain video = new VideoDomain(
                request.getTitulo(),
                request.getUrl(),
                LocalDateTime.now(),
                new PerformanceDomain(new ArrayList<>(), new ArrayList<>()),
                new CategoriaDomain(request.getCategoria()),
                usuario
        );

        videoRepository.save(video).subscribe();

    }

    public void listarVideo(){

    }

}
