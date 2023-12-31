package br.com.fiapvideo.filters;

import br.com.fiapvideo.filters.conditions.FilterConditions;
import org.springframework.data.mongodb.core.query.Criteria;

public class RecomendacaoFilterVideo extends AbstractFilter{

    @Override
    public Criteria getCriteria(FilterConditions filterConditions) {
        Criteria criteria = new Criteria();

        filterConditions.constructCriteriaConditions(criteria);

        return criteria;
    }
}
