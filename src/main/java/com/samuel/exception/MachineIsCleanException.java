package com.samuel.exception;

/**
 * Thrown to indicate that an attempt was made to wash the bathing machine
 * when it is already clean.
 * <p>
 * This class extends {@link BusinessException}.
 * </p>
 */
public class MachineIsCleanException extends BusinessException {

    /**
     * Constructs a new {@code MachineIsCleanException} with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public MachineIsCleanException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code MachineIsCleanException} with a default message indicating
     * that the machine is already clean.
     */
    public MachineIsCleanException() {
        super("A máquina já está limpa.");
    }
}
