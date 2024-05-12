package com.example.linkplustask.controller;

import com.example.linkplustask.model.Account;
import com.example.linkplustask.model.Bank;
import com.example.linkplustask.model.Transaction;
import com.example.linkplustask.model.exceptions.InvalidBankId;
import com.example.linkplustask.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.math.BigDecimal;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/bank")
public class BankController {
    @Autowired
    private BankService bankService;

    @GetMapping("/get_all")
    public ResponseEntity<?> listBanks(){
        return ResponseEntity.ok(bankService.getAllBanks());
    }

    @PostMapping("/create")
    public ResponseEntity<?> createBank(@RequestBody Bank bank){
        bankService.createBank(bank);
        return ResponseEntity.ok(bank);
    }

    @GetMapping("/bank_accounts")
    public ResponseEntity<?> getAccountsForBank(@RequestParam Long bankId){
        try {
            return ResponseEntity.ok(bankService.getBankById(bankId).getAccounts());
        }
        catch (InvalidBankId e){
            return ResponseEntity.ok(e.getMessage());
        }
    }

    @GetMapping("/bank_details")
    public ResponseEntity<?> getBankDetails(@RequestParam Long bankId){
        try {
            Bank bank = bankService.getBankById(bankId);
            List<BigDecimal> bankDetails = new ArrayList<>();
            bankDetails.add(bank.getTotalTransactionFeeAmount());
            bankDetails.add(bank.getTotalTransferAmount());
            return ResponseEntity.ok(bankDetails);
        }
        catch (InvalidBankId e){
            return ResponseEntity.ok(e.getMessage());
        }
    }
}
