package com.anf.core.services;

import com.anf.core.pojo.ResultWrapper;
import org.apache.sling.api.resource.LoginException;

import javax.jcr.RepositoryException;

public interface SearchService {
    ResultWrapper getSearchResult(String searchQueryType)
            throws RepositoryException, LoginException;
}
