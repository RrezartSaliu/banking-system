package com.example.linkplustask.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.math.BigDecimal;

@Entity
@DiscriminatorValue("withdraw_transaction")
public class WithdrawTransaction extends Transaction{
    public WithdrawTransaction(Account account, BigDecimal amount, String description) {
        super(amount, account, description);
    }

    public WithdrawTransaction() {

    }

    @Override
    public Account getDestinationAccount() {
        return null;
    }

    @Override
    public void setDestinationAccount(Account destinationAccount) {

    }
}
