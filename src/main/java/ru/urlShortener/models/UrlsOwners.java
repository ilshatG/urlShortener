package ru.urlShortener.models;

import javax.persistence.*;

@Table(
        uniqueConstraints=
        @UniqueConstraint(columnNames={"user_id", "url_id"})
)
@Entity(name = "urls_owners")
public class UrlsOwners {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "user_id", nullable = false)
    private UrlUser user;

    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "url_id", nullable = false)
    private Url url;

    public UrlsOwners() {
    }

    public UrlsOwners(UrlUser user, Url url) {
        this.user = user;
        this.url = url;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UrlUser getUser() {
        return user;
    }

    public void setUser(UrlUser user) {
        this.user = user;
    }

    public Url getUrl() {
        return url;
    }

    public void setUrl(Url url) {
        this.url = url;
    }
}
