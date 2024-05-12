package com.example.linkplustask.controller;

import com.example.linkplustask.model.Account;
import com.example.linkplustask.model.Bank;
import com.example.linkplustask.model.exceptions.InvalidAccount;
import com.example.linkplustask.model.exceptions.InvalidBankId;
import com.example.linkplustask.service.AccountService;
import com.example.linkplustask.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private BankService bankService;

    @PostMapping("/create")
    public ResponseEntity<?> createAccount(@RequestBody Account account, @RequestParam Long bankId){
        Account accountFromDB = accountService.createAccount(account);
        try {
            bankService.addAccountToBank(accountFromDB, bankId);
        }
        catch (InvalidBankId exception){
            return ResponseEntity.ok(exception.getMessage());
        }

        return ResponseEntity.ok(account);
    }

    @GetMapping("/account_balance")
    public ResponseEntity<?> checkBalanceForAccount(@RequestParam Long accountId, @RequestParam Long bankId){
        try{
            Bank bank = bankService.getBankById(bankId);
            Account account = accountService.getAccountById(accountId);
            if(!bank.getAccounts().contains(account))
                return ResponseEntity.ok("Can't check balance of other bank!");
            return ResponseEntity.ok(account.getBalance());
        }
        catch (InvalidBankId | InvalidAccount exception){
            return ResponseEntity.ok(exception.getMessage());
        }
    }
}
