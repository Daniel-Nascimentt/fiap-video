package br.com.fiapvideo.filters.conditions;

import br.com.fiapvideo.constants.ConstantsFiapVideo;
import org.springframework.data.mongodb.core.query.Criteria;

import java.time.LocalDate;
import java.util.Map;

public class VideoFilterConditions implements FilterConditions{

    private static final String FIELD_TITULO = "titulo";
    private static final String FIELD_CATEGORIA = "categoria";
    private static final String FIELD_DATAPUBLICACAO = "dataPublicacao";

    private String titulo;

    private String categoria;

    private String dataPublicacao;

    public VideoFilterConditions(Map<String, String> parameters) {
        this.titulo = parameters.get(FIELD_TITULO);
        this.categoria = parameters.get(FIELD_CATEGORIA);
        this.dataPublicacao = parameters.get(FIELD_DATAPUBLICACAO);
    }

    @Override
    public void constructCriteriaConditions(Criteria criteria) {

        if (this.titulo != null) criteria.and(FIELD_TITULO).is(this.titulo);
        if (this.categoria != null) criteria.and(FIELD_CATEGORIA).is(this.categoria);
        if (this.dataPublicacao != null) criteria.and(FIELD_DATAPUBLICACAO).gte(LocalDate.parse(dataPublicacao, ConstantsFiapVideo.DATE_TIME_FORMATTER));

    }


}
