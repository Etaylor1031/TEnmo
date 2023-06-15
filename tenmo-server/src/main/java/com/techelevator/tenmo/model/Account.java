package com.techelevator.tenmo.model;

import com.techelevator.tenmo.pojos.UserPojo;

import java.math.BigDecimal;

public class Account {
    private int accountId;
    private UserPojo user;
    private BigDecimal balance;

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public UserPojo getUser() {
        return user;
    }

    public void setUser(UserPojo user) {
        this.user = user;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
