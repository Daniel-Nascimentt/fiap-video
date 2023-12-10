package br.com.fiapvideo.web.controller;

import br.com.fiapvideo.constants.ConstantsFiapVideo;
import br.com.fiapvideo.filters.DynamicFilterVideo;
import br.com.fiapvideo.service.VideoService;
import br.com.fiapvideo.web.request.VideoRequest;
import br.com.fiapvideo.web.response.VideoResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.Map;

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

    @GetMapping(value = "/buscar")
    public Flux<VideoResponse> buscarVideosDinamicamente(
            @RequestParam(name = "page", defaultValue = ConstantsFiapVideo.DEFAULT_VALUE_PAGE) int page,
            @RequestParam(name = "size", defaultValue = ConstantsFiapVideo.DEFAULT_VALUE_SIZE) int size,
            @RequestParam(name = "sortBy", defaultValue = ConstantsFiapVideo.DEFAULT_VALUE_SORTBY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = ConstantsFiapVideo.DEFAULT_VALUE_SORTORDER) String sortOrder,
            @RequestParam(required = false) Map<String, String> parameters){

        return videoService.buscarVideosDinamicamente(new DynamicFilterVideo(page, size, sortBy, sortOrder, parameters));
    }

}
