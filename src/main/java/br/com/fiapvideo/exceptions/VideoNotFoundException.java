package br.com.fiapvideo.exceptions;

public class VideoNotFoundException extends RuntimeException{
    public VideoNotFoundException(){
        super("Vídeo não encontrado!!");
    }
}
