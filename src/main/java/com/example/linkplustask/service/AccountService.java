package com.example.linkplustask.service;

import com.example.linkplustask.model.Account;
import com.example.linkplustask.model.exceptions.InvalidAccount;
import com.example.linkplustask.model.exceptions.InvalidTransactionType;
import com.example.linkplustask.model.exceptions.NotEnoughFundsException;

import java.math.BigDecimal;

public interface AccountService {
    Account createAccount(Account account);
    void updateBalanceForTransfer(Long sourceAccountId, Long destinationAccountId, BigDecimal amount, BigDecimal feeAmount) throws NotEnoughFundsException;
    void updateBalanceForDepositOrWithdraw(Long accountId, BigDecimal amount, String transactionType) throws NotEnoughFundsException, InvalidTransactionType;
    Account getAccountById(Long id) throws InvalidAccount;
}