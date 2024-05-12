package com.example.linkplustask.model.exceptions;

public class NotEnoughFundsException extends Exception{
    public NotEnoughFundsException(){
        super("Not enough funds");
    }
}
