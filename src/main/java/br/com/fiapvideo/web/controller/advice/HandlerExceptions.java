package br.com.fiapvideo.web.controller.advice;

import br.com.fiapvideo.exceptions.PublicadorNaoCorrespondeException;
import br.com.fiapvideo.exceptions.UsuarioNotFoundException;
import br.com.fiapvideo.exceptions.VideoNotFoundException;
import br.com.fiapvideo.web.response.ErrorResponseDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestControllerAdvice
public class HandlerExceptions {

    @ExceptionHandler(UsuarioNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> usuarioNotFoundException(UsuarioNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDetails(ex.getMessage()));
    }

    @ExceptionHandler(VideoNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> videoNotFoundException(VideoNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDetails(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> methodArgumentNotValidException(MethodArgumentNotValidException ex){
        List<String> fieldsError = new ArrayList<>();

        ex.getFieldErrors().forEach(f -> fieldsError.add("PARAMETRO: [ " + f.getField() + " ] Mensagem: [ " + f.getDefaultMessage() + " ]"));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseDetails(
                "Por favor, verifique se todos os campos foram preenchidos corretamente!",
                HttpStatus.BAD_REQUEST.value(),
                fieldsError,
                new Date().getTime()));
    }

    @ExceptionHandler(PublicadorNaoCorrespondeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> publicadorNaoCorrespondeException(PublicadorNaoCorrespondeException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseDetails(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<?> genericException(Exception ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDetails("Algo inesperado aconteceu!!"));
    }

}
