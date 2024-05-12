package com.example.linkplustask.model.exceptions;

public class InvalidAccount extends Exception{
    public InvalidAccount(){
        super("There is no such account!");
    }
}
