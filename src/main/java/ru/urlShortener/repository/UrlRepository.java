package ru.urlShortener.repository;

import org.springframework.data.repository.CrudRepository;
import ru.urlShortener.models.Url;

import java.util.List;

public interface UrlRepository extends CrudRepository<Url, Long> {
    Url findByUrlAndRedirectType(String url, long redirectType);
    List<Url> findAll();
}
