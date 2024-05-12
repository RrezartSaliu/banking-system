package com.example.linkplustask.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bankId;
    private String name;
    @OneToMany
    private List<Account> accounts;
    private BigDecimal totalTransactionFeeAmount;
    private BigDecimal totalTransferAmount;
    private BigDecimal transactionFlatFeeAmount;
    private BigDecimal percentFeeValue;

    public Bank(String name, List<Account> accounts, BigDecimal totalTransactionFeeAmount, BigDecimal totalTransferAmount, BigDecimal transactionFlatFeeAmount, BigDecimal percentFeeValue) {
        this.name = name;
        this.accounts = accounts;
        this.totalTransactionFeeAmount = totalTransactionFeeAmount;
        this.totalTransferAmount = totalTransferAmount;
        this.transactionFlatFeeAmount = transactionFlatFeeAmount;
        this.percentFeeValue = percentFeeValue;
    }

    public Bank() {

    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public BigDecimal getTransactionFlatFeeAmount() {
        return transactionFlatFeeAmount;
    }


    public BigDecimal getPercentFeeValue() {
        return percentFeeValue;
    }

    public void addToTotalTransferAmount(BigDecimal amount){
        this.totalTransferAmount = this.totalTransferAmount.add(amount);
    }

    public void addToTotalTransactionFeeAmount(BigDecimal amount){
        this.totalTransactionFeeAmount = this.totalTransactionFeeAmount.add(amount);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getTotalTransactionFeeAmount() {
        return totalTransactionFeeAmount;
    }

    public BigDecimal getTotalTransferAmount() {
        return totalTransferAmount;
    }

    public Long getBankId() {
        return bankId;
    }

    public void addAccount(Account account){
        accounts.add(account);
    }
}
