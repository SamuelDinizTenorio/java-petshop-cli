package com.samuel.exception;

/**
 * Thrown to indicate that an attempt was made to clean a pet that is already clean.
 * <p>
 * This class extends {@link BusinessException}.
 * </p>
 */
public class PetIsAlreadyCleanException extends BusinessException {

    /**
     * Constructs a new {@code PetIsAlreadyCleanException} with the specified detail message.
     *
     * @param message the detail message explaining that the pet is already clean
     */
    public PetIsAlreadyCleanException(String message) {
        super(message);
    }
}
