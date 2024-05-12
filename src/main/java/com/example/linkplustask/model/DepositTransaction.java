package com.example.linkplustask.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.math.BigDecimal;

@Entity
@DiscriminatorValue("deposit_transaction")
public class DepositTransaction extends Transaction{
    public DepositTransaction(Account account, BigDecimal amount, String description) {
        super(amount, account, description);
    }

    public DepositTransaction() {
        super();
    }

    @Override
    public Account getDestinationAccount() {
        return null;
    }

    @Override
    public void setDestinationAccount(Account destinationAccount) {

    }
}
