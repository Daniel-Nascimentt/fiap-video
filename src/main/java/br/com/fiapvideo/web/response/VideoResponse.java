package br.com.fiapvideo.web.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    @JsonIgnoreProperties(value = "conta")
    private UsuarioResponse publicadoPor;

}
