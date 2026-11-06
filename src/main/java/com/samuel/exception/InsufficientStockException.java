package com.samuel.exception;

/**
 * Thrown to indicate that a supply (such as water or shampoo) has insufficient stock
 * to perform the requested operation.
 */
public class InsufficientStockException extends RuntimeException {

    /**
     * Constructs a new {@code InsufficientStockException} for the specified resource name.
     *
     * @param message the name of the resource that is insufficient (e.g., "água", "shampoo")
     */
    public InsufficientStockException(String message) {
        super(String.format("Estoque de %s insuficiente! È necessario reabastecer.", message));
    }
}
