package com.example.linkplustask.service.implementation;

import com.example.linkplustask.model.Account;
import com.example.linkplustask.model.Bank;
import com.example.linkplustask.model.FlatFeeTransaction;
import com.example.linkplustask.model.Transaction;
import com.example.linkplustask.model.exceptions.*;
import com.example.linkplustask.repository.TransactionRepository;
import com.example.linkplustask.service.AccountService;
import com.example.linkplustask.service.BankService;
import com.example.linkplustask.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Qualifier("FlatFeeTransaction")
public class FlatFeeTransactionImpl implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    private BankService bankService;

    @Override
    public Transaction createTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
        return transaction;
    }

    @Override
    public void executeTransaction(Long transactionId, Long bankId) throws NotEnoughFundsException, InvalidBankId, AccountNotFromBankException, InvalidTransaction {
        Bank bank = bankService.getBankById(bankId);
        FlatFeeTransaction flatFeeTransaction = (FlatFeeTransaction) this.getById(transactionId);
        if(bank.getAccounts().stream().map(Account::getAccountId).toList().contains(flatFeeTransaction.getSourceAccount().getAccountId())){
            Long sourceAccId = flatFeeTransaction.getSourceAccount().getAccountId();
            Long destAccId = flatFeeTransaction.getDestinationAccount().getAccountId();
            BigDecimal transactionAmount = flatFeeTransaction.getAmount();
            BigDecimal feeAmount = bank.getTransactionFlatFeeAmount();
            accountService.updateBalanceForTransfer(sourceAccId, destAccId, transactionAmount, feeAmount);
            bankService.updateTransferAmountAndTransactionFeeAmount(bankId, transactionAmount, feeAmount);
        }
        else {
            throw new AccountNotFromBankException();
        }
    }

    @Override
    public List<Transaction> getTransactionsForAccount(Long accountId) throws InvalidAccount {
        Account account = accountService.getAccountById(accountId);
        List<Transaction> allTransactions= new ArrayList<>();
        allTransactions.addAll(transactionRepository.getAllBySourceAccount(account));
        allTransactions.addAll(transactionRepository.getAllByDestinationAccount(account));
        return allTransactions;
    }

    @Override
    public Transaction getById(Long id) throws InvalidTransaction {
        return transactionRepository.findById(id).orElseThrow(InvalidTransaction::new);
    }

    @Override
    public void deleteTransaction(Long transactionId) {
        transactionRepository.deleteById(transactionId);
    }
}
