package com.example.linkplustask.service;

import java.util.List;
import com.example.linkplustask.model.Transaction;
import com.example.linkplustask.model.exceptions.*;

public interface TransactionService {
    Transaction createTransaction(Transaction transaction);
    void executeTransaction(Long transactionId, Long bankId) throws NotEnoughFundsException, InvalidTransactionType, InvalidBankId, AccountNotFromBankException, InvalidTransaction;
    List<Transaction> getTransactionsForAccount(Long accountId) throws InvalidAccount;
    Transaction getById(Long id) throws InvalidTransaction;
    void deleteTransaction(Long transactionId);
}
