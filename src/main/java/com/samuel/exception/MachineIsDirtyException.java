package com.samuel.exception;

/**
 * Thrown to indicate that an operation cannot be performed because the bathing machine is dirty.
 * The machine must be cleaned before placing another pet inside.
 * <p>
 * This class extends {@link BusinessException}.
 * </p>
 */
public class MachineIsDirtyException extends BusinessException {

    /**
     * Constructs a new {@code MachineIsDirtyException} with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public MachineIsDirtyException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code MachineIsDirtyException} with a default message indicating
     * that the machine is dirty and needs to be cleaned.
     */
    public MachineIsDirtyException() {
        super("A máquina está suja, para colocar o pet é necessário limpa-la!");
    }
}
