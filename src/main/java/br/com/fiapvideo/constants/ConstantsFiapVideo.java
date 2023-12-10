package br.com.fiapvideo.constants;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ConstantsFiapVideo {

    public static final String SORT_DEFAULT_VIDEO = "dataPublicacao";
    public static final String DEFAULT_VALUE_PAGE = "0";
    public static final String DEFAULT_VALUE_SIZE = "10";
    public static final String DEFAULT_VALUE_SORTBY = SORT_DEFAULT_VIDEO;
    public static final String DEFAULT_VALUE_SORTORDER = "ASC";
    public static final List<String> FILTERS_TO_REMOVE_MAP_PAGE_SORT = Arrays.asList("page", "size", "sortBy", "sortOrder");
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
}
