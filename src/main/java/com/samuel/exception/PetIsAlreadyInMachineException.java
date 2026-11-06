package com.samuel.exception;

/**
 * Thrown to indicate an attempt to place a pet into the bathing machine
 * when another pet is already occupying it.
 * <p>
 * This class extends {@link BusinessException}.
 * </p>
 */
public class PetIsAlreadyInMachineException extends BusinessException {

    /**
     * Constructs a new {@code PetIsAlreadyInMachineException} with the specified detail message.
     *
     * @param message the detail message explaining which pet is already in the machine
     */
    public PetIsAlreadyInMachineException(String message) {
        super(message);
    }
}
