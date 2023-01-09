/***Begin Code - Saritha Bommakanti ***/
package com.anf.core.models;


import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;
import java.util.List;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NewsFeedItems {

    @Inject
    List<NewsFeedItem> newsData;

    public List<NewsFeedItem> getItems() {
        return newsData;
    }
}

/***END Code*****/
