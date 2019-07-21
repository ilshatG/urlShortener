package ru.urlShortener.repository;

import org.springframework.data.repository.CrudRepository;
import ru.urlShortener.models.UrlUser;

public interface UserRepository extends CrudRepository<UrlUser, Long> {
    UrlUser findByLogin(String login);
}
