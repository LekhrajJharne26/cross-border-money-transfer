package com.crossborder.moneytransfer.exception;

/** Deliberately generic authentication failure that prevents account enumeration. */
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() { super("Invalid email or password"); }
}
