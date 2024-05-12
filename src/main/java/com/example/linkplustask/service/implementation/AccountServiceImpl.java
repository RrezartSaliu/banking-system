package com.example.linkplustask.service.implementation;

import com.example.linkplustask.model.Account;
import com.example.linkplustask.model.exceptions.InvalidAccount;
import com.example.linkplustask.model.exceptions.InvalidTransactionType;
import com.example.linkplustask.model.exceptions.NotEnoughFundsException;
import com.example.linkplustask.repository.AccountRepository;
import com.example.linkplustask.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public void updateBalanceForTransfer(Long sourceAccountId, Long destinationAccountId, BigDecimal amount, BigDecimal feeAmount) throws NotEnoughFundsException {
        Account sourceAcc = accountRepository.findById(sourceAccountId).orElseThrow();
        Account destAcc = accountRepository.findById(destinationAccountId).orElseThrow();
        destAcc.addToBalance(amount);
        sourceAcc.removeFromBalance(amount.add(feeAmount));
        accountRepository.save(destAcc);
        accountRepository.save(sourceAcc);
    }

    @Override
    public void updateBalanceForDepositOrWithdraw(Long accountId, BigDecimal amount, String transactionType) throws NotEnoughFundsException, InvalidTransactionType {
        Account account = accountRepository.findById(accountId).orElseThrow();
        switch (transactionType) {
            case "deposit" -> account.addToBalance(amount);
            case "withdraw" -> account.removeFromBalance(amount);
            default -> {
                //TODO throw exc
                throw new InvalidTransactionType();
            }
        }
        accountRepository.save(account);
    }

    @Override
    public Account getAccountById(Long id) throws InvalidAccount {
        return accountRepository.findById(id).orElseThrow(InvalidAccount::new);
    }
}
