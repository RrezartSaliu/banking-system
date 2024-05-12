package com.example.linkplustask.service.implementation;

import com.example.linkplustask.model.*;
import com.example.linkplustask.model.exceptions.*;
import com.example.linkplustask.repository.TransactionRepository;
import com.example.linkplustask.service.AccountService;
import com.example.linkplustask.service.BankService;
import com.example.linkplustask.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Qualifier("WithdrawTransaction")
public class WithdrawTransactionServiceImpl implements TransactionService {
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
    public void executeTransaction(Long transactionId, Long bankId) throws NotEnoughFundsException, AccountNotFromBankException, InvalidBankId, InvalidTransactionType, InvalidTransaction {
        Bank bank = bankService.getBankById(bankId);
        Transaction transaction = this.getById(transactionId);
        if(bank.getAccounts().stream().map(Account::getAccountId).toList().contains(transaction.getSourceAccount().getAccountId())){
            accountService.updateBalanceForDepositOrWithdraw(transaction.getSourceAccount().getAccountId(),transaction.getAmount(),"withdraw");
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
