package br.com.fiapvideo.exceptions;

public class UsuarioNotFoundException extends RuntimeException {

    public UsuarioNotFoundException(){
        super("Usuário não encontrado!!");
    }
}
