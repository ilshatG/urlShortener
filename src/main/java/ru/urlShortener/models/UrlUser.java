package ru.urlShortener.models;

import javax.persistence.*;

@Entity
public class UrlUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String login;
    private String password;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    public UrlUser() {
    }

    public UrlUser(UrlUser urlUser) {
        this.id = urlUser.getId();
        this.login = urlUser.getLogin();
        this.password = urlUser.getPassword();
        this.role = urlUser.getRole();
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
