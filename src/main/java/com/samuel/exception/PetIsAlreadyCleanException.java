package com.samuel.exception;

/**
 * Thrown to indicate that an attempt was made to clean a pet that is already clean.
 */
public class PetIsAlreadyCleanException extends RuntimeException {

    /**
     * Constructs a new {@code PetIsAlreadyCleanException} with the specified detail message.
     *
     * @param message the detail message explaining that the pet is already clean
     */
    public PetIsAlreadyCleanException(String message) {
        super(message);
    }
}
