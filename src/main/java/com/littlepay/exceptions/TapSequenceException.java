package com.littlepay.exceptions;

/**
 * Exception class for representing errors related to tap sequence issues.
 */
public class TapSequenceException extends RuntimeException {

    /**
     * Constructs a new TapSequenceException with the specified error message.
     *
     * @param message The error message describing the exception.
     */
    public TapSequenceException(String message) {
        super(message);
    }
}