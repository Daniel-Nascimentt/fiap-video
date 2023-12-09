package br.com.fiapvideo.filters;

import org.springframework.data.mongodb.core.query.Criteria;

public class FilterDefaultVideo extends AbstractFilter {
    public FilterDefaultVideo(int page, int size, String sortBy, String sortOrder) {
        super(page,size,sortBy, sortOrder);
    }

    @Override
    public Criteria getCriteria() {
        return new Criteria();
    }
}
