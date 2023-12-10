package br.com.fiapvideo.filters.conditions;

import org.springframework.data.mongodb.core.query.Criteria;

public interface FilterConditions {
    void constructCriteriaConditions(Criteria criteria);
}
