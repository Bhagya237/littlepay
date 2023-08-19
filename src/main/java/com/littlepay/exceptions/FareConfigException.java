package com.littlepay.exceptions;

/**
 * Exception class for representing errors related to fare configuration.
 */
public class FareConfigException extends RuntimeException {

    /**
     * Constructs a new FareConfigException with the specified error message.
     *
     * @param message The error message describing the exception.
     */
    public FareConfigException(String message) {
        super(message);
    }
}