package br.com.fiapvideo.web.controller;

import br.com.fiapvideo.service.VideoService;
import br.com.fiapvideo.web.request.VideoRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
