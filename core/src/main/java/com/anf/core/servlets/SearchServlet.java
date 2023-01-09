/***Begin Code - Saritha Bommakanti ***/
package com.anf.core.servlets;

import com.anf.core.pojo.ResultWrapper;
import com.anf.core.services.SearchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.servlet.Servlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static com.anf.core.Constant.CONTENT_TYPE;
import static com.anf.core.Constant.QUERY_TYPE;

@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.paths=/bin/search/anf-code-challenge",
                "sling.servlet.extensions=json",
                "sling.servlet.methods=POST"
        }
)
public class SearchServlet extends SlingAllMethodsServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchServlet.class);
    public static final String APPLICATION_JSON = "application/json";

    @Reference
    private transient SearchService searchService;

    @Override
    public final void doGet(final SlingHttpServletRequest request,
                             final SlingHttpServletResponse response) throws IOException {

        final PrintWriter writer = response.getWriter();
        try {
              final String queryType = request.getParameter(QUERY_TYPE);

              final ResultWrapper SearchResultObject = searchService.getSearchResult(queryType);

            ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String jsonHitAsString = objectWriter.writeValueAsString(SearchResultObject);
            JsonObject jsonObject = new JsonParser().parse(jsonHitAsString).getAsJsonObject();

            LOGGER.info("Resultant JSON returned {}", jsonObject);
            response.setHeader(CONTENT_TYPE, APPLICATION_JSON);
            writer.print(jsonObject);
        } catch (LoginException | RepositoryException e) {
            LOGGER.error("Exception in Search Servlet: {}", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}

/***END Code*****/
