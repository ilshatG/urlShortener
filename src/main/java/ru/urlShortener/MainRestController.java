package ru.urlShortener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.urlShortener.models.*;
import ru.urlShortener.repository.UrlRepository;
import ru.urlShortener.repository.UrlsOwnersRepository;
import ru.urlShortener.repository.UserRepository;
import ru.urlShortener.service.DecimalLetterCoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping
public class MainRestController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UrlRepository urlRepository;
    @Autowired
    UrlsOwnersRepository urlsOwnersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private DecimalLetterCoder decimalLetterCoder = new DecimalLetterCoder();

    @RequestMapping(value="/account", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody AccountResponse registerAccount(@RequestBody AccountRequest accountRequest) {
        AccountResponse accountResponse = new AccountResponse();
        if(accountRequest == null || accountRequest.getAccountId().trim().equals("") ||
            userRepository.findByLogin(accountRequest.getAccountId()) != null) {
            accountResponse.setSuccess(false);
            accountResponse.setDescription("Account is already exist or wrong account");
            accountResponse.setPassword("");
        } else {
            String password = new Random().ints(8, 33, 122).collect(StringBuilder::new,
                    StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
            accountResponse.setSuccess(true);
            accountResponse.setDescription("Your account is opened");
            accountResponse.setPassword(password);
            UrlUser urlUser = new UrlUser();
            urlUser.setLogin(accountRequest.getAccountId());
            urlUser.setPassword(passwordEncoder.encode(password));
            userRepository.save(urlUser);
        }
        return accountResponse;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody UrlRegisterResponse
    registerUrlResponse(HttpServletRequest request, HttpServletResponse response, Principal principal,
                        @RequestBody UrlRegisterRequest urlRegisterRequest) {
        UrlUser urlUser = new UrlUser();
        UrlRegisterResponse urlRegisterResponse = new UrlRegisterResponse();
        if (principal != null ) {
            if (urlRegisterRequest.getUrl() != null && !urlRegisterRequest.getUrl().trim().equals("")) {
                urlUser = userRepository.findByLogin(principal.getName());
                Url url = new Url(urlRegisterRequest.getUrl().trim(),
                urlRegisterRequest.getRedirectType() != 301 ? 302 : 301);
                UrlsOwners urlsOwners = new UrlsOwners();
                try {
                    urlRepository.save(url);
                } catch (DataIntegrityViolationException e) {
                    url = urlRepository.findByUrlAndRedirectType(url.getUrl(), url.getRedirectType());
                }
                try {
                    urlsOwners = urlsOwnersRepository.save(new UrlsOwners(urlUser, url));
                } catch (DataIntegrityViolationException e) {
                    urlsOwners = urlsOwnersRepository.findByUrl_IdAndUser_Id(url.getId(), urlUser.getId());
                }
                urlRegisterResponse.setSuccess(true);

                //get own domain
                String selfUrl = "";
                if (request.getServerName().endsWith("localhost")) {
                    selfUrl = String.format("%s://%s:%d/", request.getScheme(), request.getServerName(), request.getServerPort());
                } else {
                    selfUrl = String.format("%s://%s/", request.getScheme(), request.getServerName());
                }

                urlRegisterResponse.setShortUrl(selfUrl + decimalLetterCoder.decToStr(url.getId()));
            } else {
                urlRegisterResponse.setSuccess(false);
                urlRegisterResponse.setShortUrl("bad url");
            }
        } else {
            //send HTTP status code 401
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        return urlRegisterResponse;
    }

    @RequestMapping(value = "/statistic", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody List<Url> getStatistic(Principal principal) {
        List<Url> result = new ArrayList();
        if (principal != null ) {
            result = urlsOwnersRepository.findAllByUser(
                    userRepository.findByLogin(principal.getName())
            );
        }
        return result;
    }
}