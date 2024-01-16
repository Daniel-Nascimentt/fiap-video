package br.com.fiapvideo.filters.conditions;

import org.springframework.data.mongodb.core.query.Criteria;

public interface FilterConditions {
    Criteria constructCriteriaConditions(Criteria criteria);
}
