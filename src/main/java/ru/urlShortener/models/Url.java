package ru.urlShortener.models;

import ch.qos.logback.classic.db.names.ColumnName;

import javax.persistence.*;

@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"url", "redirect_type"})
)
@Entity
public class Url {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String url;

    @Column(name = "redirect_type")
    private long redirectType = 302;

    @Column(name = "redirect_counter")
    private long redirectCounter;

    public Url() {
    }

    public Url(Url url) {
        this.id = url.getId();
        this.url = url.getUrl();
        this.redirectType = url.getRedirectType();
        this.redirectCounter = url.getRedirectCounter();
    }

    public Url(String url, long redirectType) {
        this.url = url;
        this.redirectType = redirectType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getRedirectCounter() {
        return redirectCounter;
    }

    public void setRedirectCounter(long redirectCounter) {
        this.redirectCounter = redirectCounter;
    }

    public long getRedirectType() {
        return redirectType;
    }

    public void setRedirectType(long redirectType) {
        this.redirectType = redirectType;
    }

}
