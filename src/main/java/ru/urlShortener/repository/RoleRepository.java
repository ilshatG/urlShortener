package ru.urlShortener.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.urlShortener.models.Role;

import java.util.List;

public interface RoleRepository extends CrudRepository<Role, Long> {
    @Query("select b.name from UrlUser a inner join Role b on a.role.id = b.id where a.login = ?1")
    public List<String> findNameByName(String userName);
}
