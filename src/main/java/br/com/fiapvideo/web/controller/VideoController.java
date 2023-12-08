package br.com.fiapvideo.web.controller;

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

    @GetMapping
    public Flux<VideoResponse> buscarVideos(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sortBy", defaultValue = "dataPublicacao") String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "desc") String sortOrder,
            @RequestParam(name = "fieldFilter", required = false) String fieldFilter,
            @RequestParam(name = "fieldValue", required = false) String fieldValue){

        return videoService.buscarVideos(page, size, sortBy, sortOrder, fieldFilter, fieldValue);
    }

}
