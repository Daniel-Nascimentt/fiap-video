package br.com.fiapvideo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class ContaDomain {

    private String id;

    private List<VideoDomain> videosPublicados;

    private List<VideoDomain> videosAssistidos;

    private List<VideoDomain> favoritos;

}
