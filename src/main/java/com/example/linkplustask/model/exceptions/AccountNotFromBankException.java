package com.example.linkplustask.model.exceptions;

public class AccountNotFromBankException extends Exception{
    public AccountNotFromBankException(){
        super("Account is not in the bank!");
    }
}
