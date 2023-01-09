package com.anf.core.servlets;

import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import com.anf.core.Constant;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.Rendition;
import com.day.cq.dam.commons.util.DamUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.iterators.TransformIterator;
import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.anf.core.servlets.DynamicDataSourceServlet.*;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_RESOURCE_TYPES;

@Component(
        service = Servlet.class,
        property = {
                Constants.SERVICE_ID + Constant.EQUALS + SERVICE_NAME,
                SLING_SERVLET_RESOURCE_TYPES + Constant.EQUALS + RESOURCE_TYPE
        }
)
public class DynamicDataSourceServlet extends SlingSafeMethodsServlet {

    protected static final String SERVICE_NAME = "Dynamic Country DataSource Servlet";
    protected static final String RESOURCE_TYPE = "/apps/anf-code-challenge/components/form/country/dropdown";
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicDataSourceServlet.class);

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        try {
            ResourceResolver resourceResolver = request.getResourceResolver();
            Resource jsonResource = getJsonResource(resourceResolver);
            Asset asset = DamUtil.resolveToAsset(jsonResource);
            Rendition originalAsset = Objects.requireNonNull(asset).getOriginal();
            InputStream content = Objects.requireNonNull(originalAsset).adaptTo(InputStream.class);
            StringBuilder jsonContent = new StringBuilder();
            BufferedReader jsonReader = new BufferedReader(
                    new InputStreamReader(Objects.requireNonNull(content), StandardCharsets.UTF_8));
            String line;
            while ((line = jsonReader.readLine()) != null) {
                jsonContent.append(line);
            }
            JsonObject jsonObject = new JsonParser().parse(jsonContent.toString()).getAsJsonObject();
            Set keySet = jsonObject.keySet();
            Map<String, String> data = new TreeMap<>();

            Iterator<String> itr = keySet.iterator();
            while (itr.hasNext()) {
                String key = itr.next();
                data.put(key, jsonObject.get(key).getAsString());
            }
            // Creating the data source object
            @SuppressWarnings({"unchecked", "rawtypes"})
            DataSource ds = new SimpleDataSource(new TransformIterator<>(data.keySet().iterator(), (Transformer) o -> {
                String dropValue = (String) o;
                ValueMap vm = new ValueMapDecorator(new HashMap<>());
                vm.put(Constant.TEXT, dropValue);
                vm.put(Constant.VALUE, data.get(dropValue));
                return new ValueMapResource(resourceResolver, new ResourceMetadata(), JcrConstants.NT_UNSTRUCTURED, vm);
            }));
            request.setAttribute(DataSource.class.getName(), ds);
        } catch (IOException e) {
            LOGGER.error("Exception occurred: {0}", e);
        }
    }

    private Resource getJsonResource(ResourceResolver resourceResolver) {
        Resource jsonResource = resourceResolver.getResource(Constant.COUNTRIES_JSON);
        return jsonResource;
    }
}
