package br.com.fiapvideo.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PerformanceDomain {

    private List<Long> visualizacoes;

    private List<Long> adicionadoFavoritos;

}
