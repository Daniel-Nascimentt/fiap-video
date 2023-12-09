package br.com.fiapvideo.filters;

import br.com.fiapvideo.constants.ConstantsFiapVideo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractFilter {

    private int page;
    private int size;
    private String sortBy;
    private String sortOrder;

    public AbstractFilter (int page, int size, String sortBy, String sortOrder) {
        this.page = page;
        this.size = size;
        this.sortBy = sortBy;
        this.sortOrder = sortOrder;
    }

    public Pageable getPageableAndSort(){

        Sort.Direction direction = this.sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        return PageRequest.of(this.page, this.size, Sort.by(direction, this.sortBy));

    }

    public abstract Criteria getCriteria();

}
