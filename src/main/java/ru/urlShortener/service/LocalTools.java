package ru.urlShortener.service;

import javax.servlet.http.HttpServletRequest;

public class LocalTools {
    //get own domain. Depending whether it is real domain or localhost.
    public String getSelfDomain(HttpServletRequest request) {
        String selfUrl;
        if (request.getServerName().endsWith("localhost")) {
            selfUrl = String.format("%s://%s:%d/", request.getScheme(), request.getServerName(), request.getServerPort());
        } else {
            selfUrl = String.format("%s://%s/", request.getScheme(), request.getServerName());
        }
        return selfUrl;
    }
}
