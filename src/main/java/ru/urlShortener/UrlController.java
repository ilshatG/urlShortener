package ru.urlShortener;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.InternalResourceView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@Controller
@RequestMapping
public class UrlController implements ErrorController {


    @Value("classpath:static/index.html")
    Resource resourceFile;

    @RequestMapping(value = "/{shortUrl:.+}" /*":[a-zA-Z0-9]+  /{shortUrl:[.+]}"*/, method = RequestMethod.GET, produces = "html/text")
    //public ResponseEntity<String>
    public void redirect(HttpServletRequest request, HttpServletResponse response, @PathVariable String shortUrl) throws Exception {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/html;charset=UTF-8");
        if (shortUrl.equals("index.html")) {
            response.getOutputStream().write(Files.readAllBytes(Paths.get(resourceFile.getURI())));
            response.getOutputStream().close();

            //request.getRequestDispatcher("index.html").forward(request, response);
            //index();
        } else {
            /*response.getWriter().write("path variable " + shortUrl);
            response.getWriter().flush();
            response.getWriter().close();*/
            response.sendRedirect("//yandex.ru");
            //get own domain
            //String selfUrl = String.format("%s://%s:%d/",request.getScheme(),  request.getServerName(), request.getServerPort());
            //response.sendRedirect("xxx"/*+ shortUrl*/);
        }
        return;// new ResponseEntity<String>("path variable " + shortUrl + "  " + shortUrl.equals("index"), httpHeaders, HttpStatus.OK);
    }

   /* @RequestMapping(value = "/error")
    public String handleError() {
        return "index.html";
    }*/

    @Override
    public String getErrorPath() {
        return null;
    }
}
