/***Begin Code - Saritha Bommakanti ***/
package com.anf.core.services.impl;

import com.anf.core.enums.JcrQueryType;
import com.anf.core.pojo.ResultItem;
import com.anf.core.pojo.ResultWrapper;
import com.anf.core.services.ResolverService;
import com.anf.core.services.SearchService;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.jcr.query.Row;
import javax.jcr.query.RowIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.adobe.aemds.guide.utils.JcrResourceConstants.CQ_PAGE;
import static com.anf.core.Constant.*;
import static javax.jcr.query.Query.JCR_SQL2;

/**
 * Search Service
 */
@Component(service = SearchService.class, immediate = true)
public class SearchServiceImpl implements SearchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchService.class);

    @Reference
    private QueryBuilder queryBuilder;

    @Reference
    private ResolverService resolverService;

    @Override
    public ResultWrapper getSearchResult(String searchQueryType)
            throws RepositoryException, LoginException {
        final ResourceResolver resolver = resolverService.getSystemResourceResolver();
        if (JcrQueryType.SQL2.name().equals(searchQueryType)) {
            return getSearchResultUsingSql2(resolver);
        }
        return getSearchResultUsingQueryBuilder(resolver);
    }

    private ResultWrapper getSearchResultUsingQueryBuilder(final ResourceResolver resolver) throws RepositoryException {
        ResultWrapper resultWrapper = new ResultWrapper();

        final Map<String, String> predicateMap = generatePredicateMap();
        final SearchResult searchResult = executeQueryBuilderQuery(predicateMap, resolver);
        if (searchResult != null) {
            final List<Hit> hitsList = searchResult.getHits();
            if (hitsList != null && !hitsList.isEmpty()) {
                final List<ResultItem> resultObjectList = new ArrayList<>();
                for (final Hit hit : searchResult.getHits()) {
                    resultObjectList.add(generateResultObject(hit.getPath(), resolver));
                }
                resultWrapper.setResultItems(resultObjectList);
            }
        }
        return resultWrapper;
    }

    private ResultWrapper getSearchResultUsingSql2(ResourceResolver resolver) throws RepositoryException {
        final ResultWrapper resultWrapper = new ResultWrapper();
        final QueryResult queryResult = executeSql2Query(resolver);

        final RowIterator rowIterator = queryResult.getRows();
        if (rowIterator != null) {
            if (rowIterator.getSize() > 0) {
                final List<ResultItem> resultObjectList = new ArrayList<>();
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.nextRow();
                    resultObjectList.add(generateResultObject(row.getPath(), resolver));
                }
                resultWrapper.setResultItems(resultObjectList);
            }
        }
        return resultWrapper;
    }


    private SearchResult executeQueryBuilderQuery(final Map<String, String> predicateMap, final ResourceResolver resolver) {
        final Query queryObj = this.queryBuilder.createQuery(
                PredicateGroup.create(predicateMap), resolver.adaptTo(Session.class));
        return queryObj.getResult();
    }

    private QueryResult executeSql2Query(final ResourceResolver resolver) throws RepositoryException {
        QueryManager queryManager = resolver.adaptTo(Session.class).getWorkspace().getQueryManager();
        javax.jcr.query.Query query = queryManager.createQuery(ANF_SQL2_QUERY, JCR_SQL2);
        query.setLimit(LIMIT_10);
        return query.execute();

    }

    private ResultItem generateResultObject(final String path, final ResourceResolver resolver) {
        return new ResultItem(path, resolver);
    }

    private Map<String, String> generatePredicateMap() {

        final Map<String, String> predicateMap = new HashMap<>();
        predicateMap.put(PREDICATE_PATH, CONTENT_ANF_CODE_CHALLENGE_US_EN);
        predicateMap.put(PREDICATE_LIMIT, PREDICATE_LIMIT_10);
        predicateMap.put(PREDICATE_ORDERBY, JCR_SCORE_SEARCH_ATTR);
        predicateMap.put(PREDICATE_ORDERBY_SORT, PREDICATE_DESC);

        predicateMap.put(PROPERTY, JCR_CONTENT_ANF_CODE_CHALLENGE);
        predicateMap.put(PROPERTY_OPERATION, EXISTS);

        predicateMap.put(TYPE, CQ_PAGE);

        LOGGER.debug("Predicate Map : {0}", predicateMap);
        return predicateMap;
    }
}

/***END Code*****/
