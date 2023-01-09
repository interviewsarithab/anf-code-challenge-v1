package com.anf.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class NewsFeedTest {

    private static final String VAR_NEWS_ANF = "/var/commerce/products/anf-code-challenge";
    public static final String NEWS_FEED_JSON = "/newsFeedTest.json";
    public static final String ANF_CONTENT_JSON = "/anfContent.json";
    public static final String US_EN_PATH = "/content/anf-code-challenge/us/en";
    public static final String NEWSFEED_NODE_PATH = "/content/anf-code-challenge/us/en/exercise-2--news-feed-component/jcr:content/root/container/container/newsfeed";

    private final AemContext ctx = new AemContext();

    @BeforeEach
    void setup() {
        ctx.addModelsForClasses(NewsFeed.class, NewsFeedItems.class, NewsFeedItem.class);
        ctx.load().json(ANF_CONTENT_JSON, US_EN_PATH);
    }

    @Test
    public void getItems() {
        ctx.load().json(NEWS_FEED_JSON, VAR_NEWS_ANF);
        Resource resource = this.ctx.currentResource(NEWSFEED_NODE_PATH);
        ctx.currentResource(resource);

        NewsFeed newsFeed = this.ctx.currentResource().adaptTo(NewsFeed.class);
        assertEquals(10, newsFeed.getItems().size());

        NewsFeedItem newsFeedItem = newsFeed.getItems().get(0);

        assertNotNull(newsFeedItem.getTitle());
        assertNotNull(newsFeedItem.getDescription());
        assertNotNull(newsFeedItem.getAuthor());
        assertNotNull(newsFeedItem.getContent());
        assertNotNull(newsFeedItem.getUrl());
        assertNotNull(newsFeedItem.getUrlImage());
        assertNotNull(newsFeedItem.getCurrentDate());
    }

    @Test
    public void getItemsAsNullIfNewsNodeNotExists() {
        Resource resource = this.ctx.currentResource(NEWSFEED_NODE_PATH);
        ctx.currentResource(resource);

        NewsFeed newsFeed = this.ctx.currentResource().adaptTo(NewsFeed.class);
        assertNull(newsFeed.getItems());
    }
}