package com.samuel.model;

import com.samuel.exception.PetIsAlreadyCleanException;

/**
 * Represents a pet undergoing care in the pet shop.
 * Holds information regarding its name and hygiene status.
 */
public class Pet {

    private final String name;
    private boolean clean;

    /**
     * Creates a new pet with the given name.
     * The pet is initially not clean ({@code clean = false}).
     *
     * @param name the name of the pet
     */
    public Pet(String name) {
        this.name = name;
        this.clean = false;
    }

    /**
     * Returns the name of the pet.
     *
     * @return the pet's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Checks if the pet is currently clean.
     *
     * @return {@code true} if clean, {@code false} otherwise
     */
    public boolean isClean() {
        return this.clean;
    }

    /**
     * Cleans the pet.
     *
     * @throws PetIsAlreadyCleanException if the pet is already clean
     */
    public void clean() {
        if (this.clean) {
            throw new PetIsAlreadyCleanException(String.format("O pet %s já está limpo.", this.getName()));
        }
        this.clean = true;
    }
}
