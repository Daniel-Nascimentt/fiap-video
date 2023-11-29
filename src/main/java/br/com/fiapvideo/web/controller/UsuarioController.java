package br.com.fiapvideo.web.controller;

import br.com.fiapvideo.service.UsuarioService;
import br.com.fiapvideo.web.request.UsuarioRequest;
import br.com.fiapvideo.web.response.UsuarioResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public Mono<ResponseEntity<UsuarioResponse>> criarNovoUsuario(@RequestBody @Valid UsuarioRequest request){
        return Mono.just(usuarioService.criarNovoUsuario(request))
                .map(usuarioResponse -> ResponseEntity.status(HttpStatus.CREATED).body(usuarioResponse));
    }

}
