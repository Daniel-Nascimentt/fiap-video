package br.com.fiapvideo.service;


import br.com.fiapvideo.exceptions.UsuarioNotFoundException;
import br.com.fiapvideo.repository.VideoRepository;
import br.com.fiapvideo.useCases.VideoUseCase;
import br.com.fiapvideo.web.request.VideoRequest;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VideoService {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private UsuarioService usuarioService;

    public void criarVideo(@NotNull VideoRequest request) throws UsuarioNotFoundException {

       usuarioService.buscarPorEmail(request.getPublicadoPor()).subscribe(user -> {
           new VideoUseCase().criarVideo(request, user, videoRepository);
       });

    }

}
