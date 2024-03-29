package br.com.fiapvideo.useCases.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Document(value = "videos")
public class VideoDomain {

    @Id
    private String id;

    @NotBlank(message = "O vídeo precisa ter um título.")
    @Size(min = 5)
    private String titulo;

    @NotBlank(message = "O vídeo precisa ter uma URL.")
    private String url;

    @NotNull
    private LocalDateTime dataPublicacao;

    private PerformanceDomain performance;

    @NotBlank(message = "O vídeo precisa ter uma categoria.")
    private String categoria;

    @NotBlank(message = "O vídeo precisa ter uma descricao.")
    private String descricao;

    @DBRef
    @NotNull(message = "O vídeo precisa ter um publicador.")
    private UsuarioDomain publicadoPor;

    public VideoDomain(String titulo, String url, LocalDateTime dataPublicacao, PerformanceDomain performance, String categoria, String descricao, UsuarioDomain publicadoPor) {
        this.titulo = titulo;
        this.url = url;
        this.dataPublicacao = dataPublicacao;
        this.performance = performance;
        this.categoria = categoria;
        this.descricao = descricao;
        this.publicadoPor = publicadoPor;
    }

}
