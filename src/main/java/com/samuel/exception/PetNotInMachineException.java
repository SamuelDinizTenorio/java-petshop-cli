package com.samuel.exception;

/**
 * Thrown to indicate that an operation requiring a pet in the bathing machine
 * was attempted, but no pet is currently present.
 */
public class PetNotInMachineException extends RuntimeException {

    /**
     * Constructs a new {@code PetNotInMachineException} with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public PetNotInMachineException(String message) {
        super(message);
    }
}
