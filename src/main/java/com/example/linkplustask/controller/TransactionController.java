package com.example.linkplustask.controller;

import com.example.linkplustask.model.*;
import com.example.linkplustask.model.exceptions.*;
import com.example.linkplustask.service.AccountService;
import com.example.linkplustask.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    @Qualifier("FlatFeeTransaction")
    @Autowired
    private TransactionService flatFeeTransactionService;
    @Qualifier("PercentFeeTransaction")
    @Autowired
    private TransactionService percentFeeTransactionService;
    @Qualifier("DepositTransaction")
    @Autowired
    private TransactionService depositTransactionService;
    @Qualifier("WithdrawTransaction")
    @Autowired
    private TransactionService withdrawTransactionService;
    @Autowired
    private AccountService accountService;

    @PostMapping("/flat_fee")
    public ResponseEntity<?> performFlatFeeTransaction(@RequestBody FlatFeeTransaction transaction, @RequestParam Long srcAccId, @RequestParam Long destAccId, @RequestParam Long bankId){
        Long transactionIdFromDB = null;
        try{
            Account srcAcc = accountService.getAccountById(srcAccId);
            Account destAcc = accountService.getAccountById(destAccId);
            transaction.setSourceAccount(srcAcc);
            transaction.setDestinationAccount(destAcc);
            Transaction trsFromDB = flatFeeTransactionService.createTransaction(transaction);
            transactionIdFromDB = trsFromDB.getTransactionId();
            flatFeeTransactionService.executeTransaction(transactionIdFromDB, bankId);
            return ResponseEntity.ok("Flat Fee Transaction Completed Successfully");
        }
        catch (InvalidAccount | NotEnoughFundsException | InvalidTransactionType | InvalidBankId | AccountNotFromBankException | InvalidTransaction exception){
            if(transactionIdFromDB != null)
                flatFeeTransactionService.deleteTransaction(transactionIdFromDB);
            return ResponseEntity.ok(exception.getMessage());
        }
    }

    @PostMapping("/percent_fee")
    public ResponseEntity<?> performPercentFeeTransaction(@RequestBody PercentFeeTransaction transaction, @RequestParam Long srcAccId, @RequestParam Long destAccId, @RequestParam Long bankId){
        Long transactionIdFromDB = null;
        try {
            Account srcAcc = accountService.getAccountById(srcAccId);
            Account destAcc = accountService.getAccountById(destAccId);
            transaction.setSourceAccount(srcAcc);
            transaction.setDestinationAccount(destAcc);
            Transaction trsFromDB = percentFeeTransactionService.createTransaction(transaction);
            transactionIdFromDB = trsFromDB.getTransactionId();
            percentFeeTransactionService.executeTransaction(transactionIdFromDB, bankId);
            return ResponseEntity.ok("Percent Fee Transaction Completed Successfully");
        }
        catch (InvalidAccount | NotEnoughFundsException | InvalidTransactionType | InvalidBankId | AccountNotFromBankException | InvalidTransaction exception){
            if(transactionIdFromDB != null)
                percentFeeTransactionService.deleteTransaction(transactionIdFromDB);
            return ResponseEntity.ok(exception.getMessage());
        }
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> performDeposit(@RequestBody DepositTransaction depositTransaction, @RequestParam Long srcId, @RequestParam Long bankId){
        Long transactionIdFromDB = null;
        try {
            Account account = accountService.getAccountById(srcId);
            depositTransaction.setSourceAccount(account);
            Transaction trsFromDB = depositTransactionService.createTransaction(depositTransaction);
            transactionIdFromDB = trsFromDB.getTransactionId();
            depositTransactionService.executeTransaction(transactionIdFromDB, bankId);
            return ResponseEntity.ok("Deposited Successfully");
        }
        catch (InvalidAccount | NotEnoughFundsException | InvalidTransactionType | InvalidBankId | AccountNotFromBankException | InvalidTransaction exception){
            if(transactionIdFromDB != null)
                depositTransactionService.deleteTransaction(transactionIdFromDB);
            return ResponseEntity.ok(exception.getMessage());
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> performWithdraw(@RequestBody WithdrawTransaction withdrawTransaction,  @RequestParam Long srcId, @RequestParam Long bankId){
        Long transactionIdFromDB = null;
        try {
            Account account = accountService.getAccountById(srcId);
            withdrawTransaction.setSourceAccount(account);
            Transaction trsFromDB = withdrawTransactionService.createTransaction(withdrawTransaction);
            transactionIdFromDB = trsFromDB.getTransactionId();
            withdrawTransactionService.executeTransaction(transactionIdFromDB, bankId);
            return ResponseEntity.ok("Withdrawn Successfully");
        }
        catch (InvalidAccount | NotEnoughFundsException | InvalidTransactionType | InvalidBankId | AccountNotFromBankException | InvalidTransaction exception){
            if(transactionIdFromDB != null)
                withdrawTransactionService.deleteTransaction(transactionIdFromDB);
            return ResponseEntity.ok(exception.getMessage());
        }
    }

    @GetMapping("transactions_for_account")
    public ResponseEntity<?> listOfTransactionForAccount(@RequestParam Long accountId){
        try {
            List<Transaction> transactionList = flatFeeTransactionService.getTransactionsForAccount(accountId);
            return ResponseEntity.ok(transactionList);
        }
        catch (InvalidAccount invalidAccount){
            return ResponseEntity.ok(invalidAccount.getMessage());
        }
    }
}
