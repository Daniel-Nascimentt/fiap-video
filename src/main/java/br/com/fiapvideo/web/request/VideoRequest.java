package br.com.fiapvideo.web.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class VideoRequest {

    @NotBlank(message = "O vídeo precisa ter um título.")
    @Size(min = 5)
    private String titulo;

    @NotBlank(message = "O vídeo precisa ter uma categoria.")
    @Size(min = 5)
    private String categoria;

    @NotBlank(message = "Informe o e-mail do publicardor do vídeo.")
    private String publicadoPor;
}
