package com.crossborder.moneytransfer.exception;

/** Signals an attempt to create a resource that must be unique. */
public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) { super(message); }
}
