package br.com.fiapvideo.useCases.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PerformanceDomain {

    private List<Long> visualizacoes;

    private List<Long> adicionadoFavoritos;

}
