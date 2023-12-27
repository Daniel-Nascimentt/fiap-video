package br.com.fiapvideo.web.response;

import br.com.fiapvideo.useCases.domain.CategoriaDomain;
import br.com.fiapvideo.useCases.domain.PerformanceDomain;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Setter
public class VideoResponse {

    private String titulo;
    private String categoria;
    private String url;
    private LocalDateTime dataPublicacao;
    private PerformanceDomain performance;
    private UsuarioResponse publicadoPor;

    public VideoResponse(String categoria, String url, LocalDateTime dataPublicacao, PerformanceDomain performance, String titulo) {
        this.categoria = categoria;
        this.url = url;
        this.dataPublicacao = dataPublicacao;
        this.performance = performance;
        this.titulo = titulo;
    }

    public VideoResponse(String categoria, String url, LocalDateTime dataPublicacao, PerformanceDomain performance, String titulo, UsuarioResponse publicadoPor) {
        this.categoria = categoria;
        this.url = url;
        this.dataPublicacao = dataPublicacao;
        this.performance = performance;
        this.titulo = titulo;
        this.publicadoPor = publicadoPor;
    }

    @JsonBackReference
    public UsuarioResponse getPublicadoPor() {
        return publicadoPor;
    }
}
