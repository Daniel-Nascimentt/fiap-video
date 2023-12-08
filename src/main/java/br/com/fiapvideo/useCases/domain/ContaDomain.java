package br.com.fiapvideo.useCases.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Document
public class ContaDomain {

    @Id
    private String id;

    @DBRef
    private List<VideoDomain> videosPublicados;

    @DBRef
    private List<VideoDomain> videosAssistidos;

    @DBRef
    private List<VideoDomain> favoritos;

    public ContaDomain(List<VideoDomain> videosPublicados, List<VideoDomain> videosAssistidos, List<VideoDomain> favoritos) {
        this.videosPublicados = videosPublicados;
        this.videosAssistidos = videosAssistidos;
        this.favoritos = favoritos;
    }

    public void setId(String id) {
        this.id = id;
    }
}
