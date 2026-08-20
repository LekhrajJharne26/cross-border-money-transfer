package com.crossborder.moneytransfer.exception;

/** Signals that a required persisted resource was not found. */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) { super(message); }
}
