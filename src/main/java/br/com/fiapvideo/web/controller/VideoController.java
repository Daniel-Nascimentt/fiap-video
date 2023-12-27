package br.com.fiapvideo.web.controller;

import br.com.fiapvideo.constants.ConstantsFiapVideo;
import br.com.fiapvideo.filters.DynamicFilterVideo;
import br.com.fiapvideo.service.VideoService;
import br.com.fiapvideo.web.request.VideoRequest;
import br.com.fiapvideo.web.request.EspectVideoRequest;
import br.com.fiapvideo.web.response.VideoResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping(value = "/videos")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @PostMapping
    public Mono<ResponseEntity<VideoResponse>> criarVideo(@RequestBody @Valid VideoRequest request){
        return videoService.criarVideo(request).map(video -> ResponseEntity.status(HttpStatus.CREATED).body(video));
    }

    @GetMapping(value = "/buscar")
    public Flux<VideoResponse> buscarVideosDinamicamente(
            @RequestParam(name = "page", defaultValue = ConstantsFiapVideo.DEFAULT_VALUE_PAGE) int page,
            @RequestParam(name = "size", defaultValue = ConstantsFiapVideo.DEFAULT_VALUE_SIZE) int size,
            @RequestParam(name = "sortBy", defaultValue = ConstantsFiapVideo.DEFAULT_VALUE_SORTBY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = ConstantsFiapVideo.DEFAULT_VALUE_SORTORDER) String sortOrder,
            @RequestParam(required = false) Map<String, String> parameters){

        return videoService.buscarVideosDinamicamente(new DynamicFilterVideo(page, size, sortBy, sortOrder, parameters));
    }

    @GetMapping(value = "/visualizar")
    public Mono<ResponseEntity<Void>> visualizarVideo(@RequestBody @Valid EspectVideoRequest request){
        return videoService.visualizarVideo(request).map(result -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }

    @PostMapping(value = "/favoritar")
    public Mono<ResponseEntity<Void>> favoritarVideo(@RequestBody @Valid EspectVideoRequest request){
        return videoService.favoritarVideo(request).map(result -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }

}
