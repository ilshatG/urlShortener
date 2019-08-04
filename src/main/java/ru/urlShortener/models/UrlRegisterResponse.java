package ru.urlShortener.models;

public class UrlRegisterResponse {
    private boolean success;
    private String shortUrl;

    public UrlRegisterResponse() {
    }

    public UrlRegisterResponse(boolean success, String shortUrl) {
        this.success = success;
        this.shortUrl = shortUrl;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }
}
