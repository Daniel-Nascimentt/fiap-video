package br.com.fiapvideo.filters;

import org.springframework.data.mongodb.core.query.Criteria;

import java.util.Map;

public class FilterByTituloVideo extends AbstractFilter{

    private static final String FIELD_TITULO = "titulo";
    private String titulo;

    public FilterByTituloVideo(int page, int size, String sortBy, String sortOrder, String titulo) {
        super(page, size, sortBy, sortOrder);
        this.titulo = titulo;
    }

    @Override
    public Criteria getCriteria() {

        Criteria criteria = new Criteria();

        criteria.and(FIELD_TITULO).is(this.titulo);

        return criteria;
    }
}
