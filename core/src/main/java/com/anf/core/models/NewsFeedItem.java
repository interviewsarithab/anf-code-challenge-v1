package com.anf.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.anf.core.Constant.DATE_FORMAT_DDMMYYYY;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NewsFeedItem {

    @Inject
    private String title;

    @Inject
    private String url;

    @Inject
    private String urlImage;

    @Inject
    private String author;

    @Inject
    private String content;

    @Inject
    private String description;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_DDMMYYYY);

    private String currentDate = simpleDateFormat.format(new Date());


    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getDescription() {
        return description;
    }

}
