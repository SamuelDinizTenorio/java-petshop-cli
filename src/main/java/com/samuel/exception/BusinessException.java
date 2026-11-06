package com.samuel.exception;

/**
 * Base abstract exception representing business and domain rule violations in the pet shop system.
 * <p>
 * All domain-specific runtime exceptions extend this class.
 * </p>
 */
public abstract class BusinessException extends RuntimeException {

    /**
     * Constructs a new {@code BusinessException} with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    protected BusinessException(String message) {
        super(message);
    }
}
