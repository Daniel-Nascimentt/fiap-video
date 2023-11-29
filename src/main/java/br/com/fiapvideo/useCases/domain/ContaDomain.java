package br.com.fiapvideo.useCases.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Document
public class ContaDomain {

    private String id;

    private List<VideoDomain> videosPublicados = new ArrayList<>();

    private List<VideoDomain> videosAssistidos = new ArrayList<>();

    private List<VideoDomain> favoritos = new ArrayList<>();

}
