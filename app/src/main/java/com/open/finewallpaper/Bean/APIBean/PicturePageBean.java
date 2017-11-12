package com.open.finewallpaper.Bean.APIBean;

import java.util.List;

/**
 * Created by yaojian on 2017/7/23.
 */

public class PicturePageBean {
    private String allPages;
    private List<PictureContentList> contentlist;
    private String currentPage;
    private String allNum;
    private String maxResult;

    public String getAllPages() {
        return allPages;
    }

    public void setAllPages(String allPages) {
        this.allPages = allPages;
    }

    public List<PictureContentList> getContentlist() {
        return contentlist;
    }

    public void setContentlist(List<PictureContentList> contentlist) {
        this.contentlist = contentlist;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public String getAllNum() {
        return allNum;
    }

    public void setAllNum(String allNum) {
        this.allNum = allNum;
    }

    public String getMaxResult() {
        return maxResult;
    }

    public void setMaxResult(String maxResult) {
        this.maxResult = maxResult;
    }
}
