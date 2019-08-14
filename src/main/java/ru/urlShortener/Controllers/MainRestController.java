package ru.urlShortener.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.urlShortener.models.*;
import ru.urlShortener.repository.UrlRepository;
import ru.urlShortener.repository.UrlsOwnersRepository;
import ru.urlShortener.repository.UserRepository;
import ru.urlShortener.service.DecimalLetterCoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.urlShortener.service.LocalTools;

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
    private static final Logger logger = LoggerFactory.getLogger(GetAndRedirectController.class);
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
        UrlUser urlUser;
        UrlRegisterResponse urlRegisterResponse = new UrlRegisterResponse();
        if (principal != null ) {
            if (urlRegisterRequest.getUrl() != null && !urlRegisterRequest.getUrl().trim().equals("")) {
                urlUser = userRepository.findByLogin(principal.getName());
                Url url = new Url(urlRegisterRequest.getUrl().trim(),
                urlRegisterRequest.getRedirectType() != 301 ? 302 : 301);
                try {
                    urlRepository.save(url);
                } catch (DataIntegrityViolationException e) {
                    url = urlRepository.findByUrlAndRedirectType(url.getUrl(), url.getRedirectType());
                }
                try {
                    urlsOwnersRepository.save(new UrlsOwners(urlUser, url));
                } catch (DataIntegrityViolationException e) {
                    //if such url exists on this user database won't add this url and throws exception. That's why this block is empty.
                    // Because that what i want.
                }
                urlRegisterResponse.setSuccess(true);
                String selfUrl = (new LocalTools()).getSelfDomain(request);
                //logger.warn("Main rest controller. Url id " + url.getId());
                //logger.warn("Main rest controller. Coded id " + decimalLetterCoder.decToStr(url.getId()));
                //logger.warn("Main rest controller. UnCoded id " + decimalLetterCoder.strToDec( decimalLetterCoder.decToStr(url.getId()) ));
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