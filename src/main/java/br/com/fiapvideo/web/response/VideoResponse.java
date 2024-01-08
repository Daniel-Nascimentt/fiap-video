package br.com.fiapvideo.web.response;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Setter
public class VideoResponse {

    private String id;
    private String titulo;
    private String categoria;
    private String url;
    private LocalDateTime dataPublicacao;
    private PerformanceResponse performance;
    private UsuarioResponse publicadoPor;

    /**
     * Esse getter foi declarado para não gerar erro ao vonverter Domain para Response
     * e resultar em um erro de dependencia ciclica.
     */
    @JsonBackReference
    @Deprecated
    public UsuarioResponse getPublicadoPor() {
        return publicadoPor;
    }
}
