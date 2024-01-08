package br.com.fiapvideo.web.controller;

import br.com.fiapvideo.service.UsuarioService;
import br.com.fiapvideo.useCases.UsuarioUseCase;
import br.com.fiapvideo.web.request.UsuarioRequest;
import br.com.fiapvideo.web.response.UsuarioResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public Mono<ResponseEntity<UsuarioResponse>> criarNovoUsuario(@RequestBody @Valid UsuarioRequest request){

        return usuarioService.criarNovoUsuario(request)
                .map(user -> ResponseEntity.status(HttpStatus.CREATED).body(user))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping
    public Mono<ResponseEntity<UsuarioResponse>> atualizarUsuario(
            @RequestParam(name = "emailAtual", required = true) String emailAtual,
            @RequestBody @Valid UsuarioRequest request){

        return usuarioService.atualizarUsuario(emailAtual, request)
                .map(usuario -> ResponseEntity.status(HttpStatus.OK).body(usuario));

    }

    @GetMapping
    public Mono<ResponseEntity<UsuarioResponse>> buscarUsuarioPorEmail(@RequestParam(name = "email", required = true) String email){
        return usuarioService.buscarPorEmail(email).map(usuario ->
                ResponseEntity.status(HttpStatus.FOUND).body(usuarioService.converterDomainParaResponse(usuario)));
    }

    @DeleteMapping
    public Mono<ResponseEntity<Void>> removerUsuarioPorEmail(@RequestParam(name = "email", required = true) String email){
        return usuarioService.removerUsuarioPorEmail(email).then(Mono.fromCallable(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).build()));
    }

}
