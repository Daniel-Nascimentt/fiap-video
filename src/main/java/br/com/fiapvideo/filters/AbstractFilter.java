package br.com.fiapvideo.filters;

import br.com.fiapvideo.constants.ConstantsFiapVideo;
import br.com.fiapvideo.filters.conditions.FilterConditions;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.Map;


public abstract class AbstractFilter {

    private Map<String, String> parameters;
    private int page;
    private int size;
    private String sortBy;
    private String sortOrder;

    public AbstractFilter (int page, int size, String sortBy, String sortOrder, Map<String, String> parameters) {
        this.page = page;
        this.size = size;
        this.sortBy = sortBy;
        this.sortOrder = sortOrder;
        ConstantsFiapVideo.FILTERS_TO_REMOVE_MAP_PAGE_SORT.forEach(parameters.keySet()::remove);
        this.parameters = parameters;
    }


    public Pageable getPageableAndSort(){

        Sort.Direction direction = this.sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        return PageRequest.of(this.page, this.size, Sort.by(direction, this.sortBy));

    }

    public abstract Criteria getCriteria(FilterConditions filterConditions);

    public Map<String, String> getParameters() {
        return parameters;
    }
}
