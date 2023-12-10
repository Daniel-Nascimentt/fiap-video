package br.com.fiapvideo.filters;

import br.com.fiapvideo.filters.conditions.FilterConditions;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.Map;


public class DynamicFilterVideo extends AbstractFilter{

    public DynamicFilterVideo(int page, int size, String sortBy, String sortOrder, Map<String, String> parameters) {
        super(page, size, sortBy, sortOrder, parameters);
    }

    @Override
    public Criteria getCriteria(FilterConditions filterConditions) {

        Criteria criteria = new Criteria();

        filterConditions.constructCriteriaConditions(criteria);

        return criteria;
    }
}
