package com.anf.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.anf.core.Constant.VAR_COMMERCE_PRODUCTS_ANF_CODE_CHALLENGE;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NewsFeed {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewsFeed.class);

    @SlingObject
    private Resource resource;

    public List<NewsFeedItem> getItems() {
        final Resource anfResource = resource.getResourceResolver().resolve(VAR_COMMERCE_PRODUCTS_ANF_CODE_CHALLENGE);
        if (anfResource.isResourceType(Resource.RESOURCE_TYPE_NON_EXISTING)) {
            LOGGER.debug("Required node do not exists {0}", VAR_COMMERCE_PRODUCTS_ANF_CODE_CHALLENGE);
            return null;
        }
        final NewsFeedItems newsFeedItems = anfResource.adaptTo(NewsFeedItems.class);
        return newsFeedItems.getItems();
    }
}
