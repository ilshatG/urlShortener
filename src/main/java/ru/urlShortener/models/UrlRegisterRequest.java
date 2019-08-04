package ru.urlShortener.models;

public class UrlRegisterRequest {
    private String url;
    private long redirectType;

    public UrlRegisterRequest(String url, long redirectType) {
        this.url = url;
        this.redirectType = redirectType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getRedirectType() {
        return redirectType;
    }

    public void setRedirectType(long redirectType) {
        this.redirectType = redirectType;
    }
}
