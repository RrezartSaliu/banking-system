package com.example.linkplustask.service.implementation;

import com.example.linkplustask.model.Account;
import com.example.linkplustask.model.Bank;
import com.example.linkplustask.model.exceptions.InvalidBankId;
import com.example.linkplustask.repository.BankRepository;
import com.example.linkplustask.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BankServiceImpl implements BankService {
    @Autowired
    private BankRepository bankRepository;

    @Override
    public Bank createBank(Bank bank) {
        bankRepository.save(bank);
        return bank;
    }

    @Override
    public Bank getBankById(Long bankId) throws InvalidBankId{
        return bankRepository.findById(bankId).orElseThrow(InvalidBankId::new);
    }

    @Override
    public List<Bank> getAllBanks() {
        return bankRepository.findAll();
    }

    @Override
    public void updateTransferAmountAndTransactionFeeAmount(Long bankId, BigDecimal transferAmount, BigDecimal transactionFeeAmount) throws InvalidBankId {
        Bank bank = this.getBankById(bankId);
        bank.addToTotalTransactionFeeAmount(transactionFeeAmount);
        bank.addToTotalTransferAmount(transferAmount);
        bankRepository.save(bank);
    }

    @Override
    public void addAccountToBank(Account account, Long bankId) throws InvalidBankId {
        Bank bank = bankRepository.findById(bankId).orElseThrow(()-> new InvalidBankId());
        bank.addAccount(account);
        bankRepository.save(bank);
    }
}
