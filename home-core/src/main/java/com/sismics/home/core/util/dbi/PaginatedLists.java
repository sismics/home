package com.sismics.home.core.util.dbi;

import java.util.List;

import org.skife.jdbi.v2.Query;
import org.skife.jdbi.v2.util.IntegerMapper;

/**
 * Utilities for paginated lists.
 * 
 * @author jtremeaux
 */
public class PaginatedLists {
    /**
     * Default size of a page.
     */
    private static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * Maximum size of a page.
     */
    private static final int MAX_PAGE_SIZE = 100;

    /**
     * Constructs a paginated list.
     * 
     * @param pageSize Size of the page
     * @param offset Offset of the page
     * @return Paginated list
     */
    public static <E> PaginatedList<E> create(Integer pageSize, Integer offset) {
        if (pageSize == null) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        if (offset == null) {
            offset = 0;
        }
        if (pageSize > MAX_PAGE_SIZE) {
            pageSize = MAX_PAGE_SIZE;
        }
        return new PaginatedList<E>(pageSize, offset);
    }
    
    /**
     * Constructs a paginated list with default parameters.
     * 
     * @return Paginated list
     */
    public static <E> PaginatedList<E> create() {
        return create(null, null);
    }
    
    /**
     * Executes a native count(*) request to count the number of results.
     * 
     * @param paginatedList Paginated list object containing parameters, and into which results are added by side effects
     * @param queryParam Query parameters
     */
    public static <E> void executeCountQuery(PaginatedList<E> paginatedList, QueryParam queryParam) {
        StringBuilder sb = new StringBuilder("select count(*) as result_count from (");
        sb.append(queryParam.getQueryString());
        sb.append(") as t1");
        
        QueryParam countQueryParam = new QueryParam(sb.toString(), queryParam.getParameterMap());
        
        Integer resultCount = QueryUtil.getNativeQuery(countQueryParam).map(IntegerMapper.FIRST).first();
        paginatedList.setResultCount(resultCount);
    }

    /**
     * Executes a query and returns the data of the currunt page.
     * 
     * @param paginatedList Paginated list object containing parameters, and into which results are added by side effects
     * @param queryParam Query parameters
     * @return List of results
     */
    private static <E> List<Object[]> executeResultQuery(PaginatedList<E> paginatedList, QueryParam queryParam) {
        Query<Object[]> q = QueryUtil.getNativeQuery(queryParam).map(ColumnIndexMapper.INSTANCE);
        
        //q.setOffset(paginatedList.getOffset()); // TODO how to do offset?
        q.setMaxRows(paginatedList.getLimit());
        return q.list();
    }
    
    /**
     * Executes a paginated request with 2 native queries (one to count the number of results, and one to return the page).
     * 
     * @param paginatedList Paginated list object containing parameters, and into which results are added by side effects
     * @param queryParam Query parameters
     * @param doCount Count the total number of rows
     * @return List of results
     */
    public static <E> List<Object[]> executePaginatedQuery(PaginatedList<E> paginatedList, QueryParam queryParam, boolean doCount) {
        if (doCount) {
            executeCountQuery(paginatedList, queryParam);
        }
        return executeResultQuery(paginatedList, queryParam);
    }

    /**
     * Executes a paginated request with 2 native queries (one to count the number of results, and one to return the page).
     * 
     * @param paginatedList Paginated list object containing parameters, and into which results are added by side effects
     * @param queryParam Query parameters
     * @param sortCriteria Sort criteria
     * @param doCount Count the total number of rows
     * @return List of results
     */
    public static <E> List<Object[]> executePaginatedQuery(PaginatedList<E> paginatedList, QueryParam queryParam, SortCriteria sortCriteria, boolean doCount) {
        StringBuilder sb = new StringBuilder(queryParam.getQueryString());
        sb.append(" order by c");
        sb.append(sortCriteria.getColumn());
        sb.append(sortCriteria.isAsc() ? " asc" : " desc");
        
        QueryParam sortedQueryParam = new QueryParam(sb.toString(), queryParam.getParameterMap());

        if (doCount) {
            executeCountQuery(paginatedList, sortedQueryParam);
        }
        return executeResultQuery(paginatedList, sortedQueryParam);
    }
}