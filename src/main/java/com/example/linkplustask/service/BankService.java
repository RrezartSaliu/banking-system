package com.example.linkplustask.service;

import com.example.linkplustask.model.Account;
import com.example.linkplustask.model.Bank;
import com.example.linkplustask.model.exceptions.InvalidBankId;
import java.util.List;
import java.math.BigDecimal;

public interface BankService {
    Bank createBank(Bank bank);
    Bank getBankById(Long bankId) throws InvalidBankId;
    List<Bank> getAllBanks();
    void updateTransferAmountAndTransactionFeeAmount(Long bankId, BigDecimal transferAmount, BigDecimal transactionFeeAmount) throws InvalidBankId;
    void addAccountToBank(Account account, Long bankId) throws InvalidBankId;
}
