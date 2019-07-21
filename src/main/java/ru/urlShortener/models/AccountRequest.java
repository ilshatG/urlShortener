package ru.urlShortener.models;

//for post request to create account
public class AccountRequest {
    private String accountId;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
