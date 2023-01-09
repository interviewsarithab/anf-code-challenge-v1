package com.anf.core.pojo;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.resource.ResourceResolver;

public class ResultItem {

    private String resultTitle;

    private String resultPath;

    public ResultItem(String path, ResourceResolver resolver) {
        PageManager pageManager = resolver.adaptTo(PageManager.class);
        Page hitPage = pageManager.getContainingPage(path);
        this.resultPath = path + ".html";
        this.resultTitle = hitPage != null ? hitPage.getTitle() : hitPage.getPageTitle();
    }

    public String getResultTitle() {
        return resultTitle;
    }

    public String getResultPath() {
        return resultPath;
    }
}
