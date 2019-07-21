package ru.urlShortener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.urlShortener.models.*;
import ru.urlShortener.repository.UserRepository;

import java.security.Principal;
import java.util.Random;

@RestController
@RequestMapping
public class MainController {
    @Autowired
    UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/account")
    public String showItems(ModelMap model, Principal principal) {
        UrlUser urlUser = new UrlUser();
        urlUser.setLogin("Admin");
        urlUser.setPassword("password");
        userRepository.save(urlUser);
        return "Hello index";
    }

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
    public @ResponseBody
    String registerUrlResponce(@RequestBody UrlRegisterRequest urlRegisterRequest) {
        return null;
    }
}
