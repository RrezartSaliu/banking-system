package com.example.linkplustask.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;
    protected BigDecimal amount;
    @ManyToOne
    @JoinColumn(name = "source_account_account_id")
    protected Account sourceAccount;
    @Column(length = 500)
    protected String description;

    public Transaction(BigDecimal amount, Account sourceAccount, String description) {
        this.amount = amount;
        this.sourceAccount = sourceAccount;
        this.description = description;
    }

    public Transaction() {

    }

    public Account getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(Account sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public abstract Account getDestinationAccount();

    public abstract void setDestinationAccount(Account destinationAccount);

    public String getDescription() {
        return description;
    }

}
