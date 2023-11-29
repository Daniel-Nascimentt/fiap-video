package br.com.fiapvideo.domain;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class VideoDomain {

    private String id;

    @NotBlank(message = "O vídeo precisa ter um título.")
    @Min(value = 5)
    private String titulo;

    @NotBlank(message = "O vídeo precisa ter uma URL.")
    private String url;

    @NotNull
    private LocalDateTime dataPublicacao;

    private PerformanceDomain performance;

    @NotNull(message = "O vídeo precisa ter uma categoria.")
    private CategoriaDomain categoria;

}
