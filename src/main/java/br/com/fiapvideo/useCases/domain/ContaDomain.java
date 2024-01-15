package br.com.fiapvideo.useCases.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Document(value = "contas")
@NoArgsConstructor /*Para uso do Spring Data*/
public class ContaDomain {

    @Id
    private String id;

    @DBRef
    private List<VideoDomain> videosPublicados;

    @DBRef
    private List<VideoDomain> videosAssistidos;

    @DBRef
    private List<VideoDomain> favoritos;

    public ContaDomain(String id, List<VideoDomain> videosPublicados, List<VideoDomain> videosAssistidos, List<VideoDomain> favoritos) {
        this.id = id;
        this.videosPublicados = videosPublicados;
        this.videosAssistidos = videosAssistidos;
        this.favoritos = favoritos;
    }

    public ContaDomain(List<VideoDomain> videosPublicados, List<VideoDomain> videosAssistidos, List<VideoDomain> favoritos) {
        this.videosPublicados = videosPublicados;
        this.videosAssistidos = videosAssistidos;
        this.favoritos = favoritos;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void addAssistido(VideoDomain videoDomain) {
        this.videosAssistidos.add(videoDomain);
    }

    public void addPublicado(VideoDomain video) {
        this.videosPublicados.add(video);
    }

    public void addFavorito(VideoDomain videoDomain) {
        this.favoritos.add(videoDomain);
    }

    public VideoDomain getUltimoFavorito() {
        return this.favoritos.get(this.favoritos.size() -1);
    }
}
