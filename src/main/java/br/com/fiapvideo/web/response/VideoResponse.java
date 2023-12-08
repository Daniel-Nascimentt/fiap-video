package br.com.fiapvideo.web.response;

import br.com.fiapvideo.useCases.domain.CategoriaDomain;
import br.com.fiapvideo.useCases.domain.PerformanceDomain;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class VideoResponse {

    private String titulo;
    private String categoria;
    private String url;
    private LocalDateTime dataPublicacao;
    private PerformanceDomain performance;
    private String emailPublicador;

    public VideoResponse(String categoria, String url, LocalDateTime dataPublicacao, PerformanceDomain performance, String titulo, String emailPublicador) {
        this.categoria = categoria;
        this.url = url;
        this.dataPublicacao = dataPublicacao;
        this.performance = performance;
        this.titulo = titulo;
        this.emailPublicador = emailPublicador;
    }
}
