package com.example.linkplustask.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue("percent_fee_transaction")
public class PercentFeeTransaction extends Transaction{
    @ManyToOne
    private Account destinationAccount;

    public PercentFeeTransaction(Account sourceAccount, Account destinationAccount, BigDecimal amount, String description) {
        super(amount, sourceAccount, description);
        this.destinationAccount = destinationAccount;
    }

    public PercentFeeTransaction() {

    }

    @Override
    public Account getDestinationAccount() {
        return destinationAccount;
    }

    @Override
    public void setDestinationAccount(Account destinationAccount) {
        this.destinationAccount = destinationAccount;
    }

}
