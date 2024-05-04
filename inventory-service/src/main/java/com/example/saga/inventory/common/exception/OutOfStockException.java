package com.example.saga.inventory.common.exception;

public class OutOfStockException extends RuntimeException{
    private static final String MESSAGE="out of stock";

    public OutOfStockException() {
        super(MESSAGE);
    }
}
