package br.com.fiapvideo.useCases.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Document
public class ContaDomain {

    private String id;

    private List<VideoDomain> videosPublicados;

    private List<VideoDomain> videosAssistidos;

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
