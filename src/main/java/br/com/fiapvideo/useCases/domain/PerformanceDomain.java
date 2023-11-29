package br.com.fiapvideo.useCases.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Document
public class PerformanceDomain {

    private List<Long> visualizacoes;

    private List<Long> adicionadoFavoritos;

}
