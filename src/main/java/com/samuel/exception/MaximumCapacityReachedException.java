package com.samuel.exception;

/**
 * Thrown to indicate that a supply container (e.g., water or shampoo reservoir)
 * has already reached its maximum storage capacity and cannot be refilled further.
 */
public class MaximumCapacityReachedException extends RuntimeException {

    /**
     * Constructs a new {@code MaximumCapacityReachedException} for the specified resource name.
     *
     * @param message the name of the resource that reached capacity (e.g., "água", "shampoo")
     */
    public MaximumCapacityReachedException(String message) {
        super(String.format("Capacida máxima de %s atingida!", message));
    }
}
