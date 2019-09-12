package ru.urlShortener.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.urlShortener.models.Url;
import ru.urlShortener.repository.UrlRepository;
import ru.urlShortener.service.DecimalLetterCoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.urlShortener.service.LocalTools;

@Controller
@RequestMapping
public class GetAndRedirectController implements ErrorController {


    @Value("classpath:static/index.html")
    Resource resourceFile;

    @Autowired
    UrlRepository urlRepository;
    private static final Logger logger = LoggerFactory.getLogger(GetAndRedirectController.class);
    private DecimalLetterCoder decimalLetterCoder = new DecimalLetterCoder();

    @RequestMapping(value = "/{shortUrl:.+}", method = RequestMethod.GET, produces = "html/text")
    public void redirect(HttpServletRequest request, HttpServletResponse response, @PathVariable String shortUrl) throws Exception {
        logger.warn("Enter get controller of URL Shortener... " + shortUrl);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/html;charset=UTF-8");
        if (shortUrl.equals("index.html") || shortUrl.equals("error")) {
            response.getOutputStream().println("Hello world");
            //response.getOutputStream().write(Files.readAllBytes(Paths.get(resourceFile.getURI())));
            response.getOutputStream().close();
        } else {
            long urlId = decimalLetterCoder.strToDec(shortUrl);
            logger.warn("Url id: " + shortUrl);
            Url url;
            if (urlId > 0) {
                Optional<Url> opt  = urlRepository.findById(urlId);
                //logger.warn("Url is " + opt.get().getUrl());
                if (opt.isPresent()) {
                    url = opt.get();
                    sendRedirect(response, url.getUrl(), url.getRedirectType());
                    url.inc();
                    urlRepository.save(url);
                    return;
                }
            }
            logger.warn("Redirect to self");
            sendRedirect(response, (new LocalTools()).getSelfDomain(request), 301);
        }
        return;
    }

    @Override
    public String getErrorPath() {
        return null;
    }

    private void sendRedirect(HttpServletResponse response, String url, long redirect) {
        response.setStatus(redirect == 301 ? HttpServletResponse.SC_MOVED_PERMANENTLY :
                HttpServletResponse.SC_MOVED_TEMPORARILY );
        response.setHeader("Location", url);
        response.setHeader("Connection", "close");
    }
}
