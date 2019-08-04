package ru.urlShortener.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.urlShortener.models.Url;
import ru.urlShortener.models.UrlUser;
import ru.urlShortener.models.UrlsOwners;

import java.util.List;

public interface UrlsOwnersRepository extends CrudRepository<UrlsOwners, Long> {
    UrlsOwners findByUrl_IdAndUser_Id(long url_id, long user_id);

    @Query("select new ru.urlShortener.models.Url(a.url) from ru.urlShortener.models.UrlsOwners a " +
            " where a.user = :user")
    List<Url> findAllByUser(@Param("user") UrlUser urlUser);
}
