package com.example.linkplustask.model;

import com.example.linkplustask.model.exceptions.NotEnoughFundsException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;
    private String userName;
    private BigDecimal balance;

    public Account(String userName, BigDecimal balance) {
        this.userName = userName;
        this.balance = balance;
    }

    public Account() {

    }

    public void addToBalance(BigDecimal amount){
        balance = balance.add(amount);
    }

    public void removeFromBalance(BigDecimal amount)throws NotEnoughFundsException {
        if(balance.compareTo(amount) < 0)
            throw new NotEnoughFundsException();
        balance = balance.subtract(amount);
    }

    public Long getAccountId() {
        return accountId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getUserName() {
        return userName;
    }

}
