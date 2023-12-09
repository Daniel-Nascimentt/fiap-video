package br.com.fiapvideo.web.controller;

import br.com.fiapvideo.constants.ConstantsFiapVideo;
import br.com.fiapvideo.filters.FilterByTituloVideo;
import br.com.fiapvideo.filters.FilterDefaultVideo;
import br.com.fiapvideo.service.VideoService;
import br.com.fiapvideo.web.request.VideoRequest;
import br.com.fiapvideo.web.response.VideoResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping(value = "/videos")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @PostMapping
    public ResponseEntity<?> criarVideo(@RequestBody @Valid VideoRequest request){
        videoService.criarVideo(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public Flux<VideoResponse> buscarVideosPorTitulo(
            @RequestParam(name = "page", defaultValue = ConstantsFiapVideo.DEFAULT_VALUE_PAGE) int page,
            @RequestParam(name = "size", defaultValue = ConstantsFiapVideo.DEFAULT_VALUE_SIZE) int size,
            @RequestParam(name = "sortBy", defaultValue = ConstantsFiapVideo.DEFAULT_VALUE_SORTBY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = ConstantsFiapVideo.DEFAULT_VALUE_SORTORDER) String sortOrder){

        return videoService.buscarVideos(new FilterDefaultVideo(page, size, sortBy, sortOrder));
    }

    @GetMapping(value = "/buscarPorTitulo")
    public Flux<VideoResponse> buscarVideosPorTitulo(
            @RequestParam(name = "page", defaultValue = ConstantsFiapVideo.DEFAULT_VALUE_PAGE) int page,
            @RequestParam(name = "size", defaultValue = ConstantsFiapVideo.DEFAULT_VALUE_SIZE) int size,
            @RequestParam(name = "sortBy", defaultValue = ConstantsFiapVideo.DEFAULT_VALUE_SORTBY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = ConstantsFiapVideo.DEFAULT_VALUE_SORTORDER) String sortOrder,
            @RequestParam(name = "titulo", required = true) String titulo){

        return videoService.buscarVideos(new FilterByTituloVideo(page, size, sortBy, sortOrder, titulo));
    }

}
