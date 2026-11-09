package com.samuel.model;

import com.samuel.exception.MaximumCapacityReachedException;

import java.util.Optional;

import com.samuel.exception.InsufficientStockException;
import com.samuel.exception.MachineIsCleanException;
import com.samuel.exception.MachineIsDirtyException;
import com.samuel.exception.PetNotInMachineException;
import com.samuel.exception.PetIsAlreadyInMachineException;

/**
 * Represents an automated pet bathing machine.
 * Manages machine state, water and shampoo levels, and pet washing procedures.
 */
public class PetBathingMachine {

    private boolean clean;
    private Double water;
    private Double shampoo;
    private Pet pet;

    /**
     * Initializes a new pet bathing machine in a clean state
     * with 30.0 liters of water and 10.0 liters of shampoo.
     */
    public PetBathingMachine() {
        this.clean = true;
        this.water = 30.0;
        this.shampoo = 10.0;
    }

    /**
     * Gives a bath to the pet currently inside the machine.
     * Consumes 10.0 units of water and 2.0 units of shampoo, then marks the pet as clean.
     *
     * @throws PetNotInMachineException   if there is no pet in the machine
     * @throws InsufficientStockException  if there is not enough water or shampoo
     */
    public void takeAShowerInPet() {
        if (!hasPet()) throw new PetNotInMachineException("Coloque o pet na máquina para iniciar o banho");
        if (this.water < 10.0) throw new InsufficientStockException("água");
        if (this.shampoo < 2.0) throw new InsufficientStockException("shampoo");
        this.water -= 10.0;
        this.shampoo -= 2.0;
        pet.clean();
    }    

    /**
     * Returns the current water level in the machine.
     *
     * @return the amount of water available
     */
    public Double getWater() {
        return this.water;
    }

    /**
     * Adds 2.0 units of water to the reservoir.
     *
     * @throws MaximumCapacityReachedException if water level is already at maximum capacity (30.0)
     */
    public void addWater() {
        if (this.water == 30.0) throw new MaximumCapacityReachedException("água");
        this.water += 2.0;
    }

    /**
     * Returns the current shampoo level in the machine.
     *
     * @return the amount of shampoo available
     */
    public Double getShampoo() {
        return this.shampoo;
    }

    /**
     * Adds 2.0 units of shampoo to the reservoir.
     *
     * @throws MaximumCapacityReachedException if shampoo level is already at maximum capacity (10.0)
     */
    public void addShampoo() {
        if (this.shampoo == 10.0) throw new MaximumCapacityReachedException("shampoo");
        this.shampoo += 2.0;
    }

    /**
     * Retrieves the pet currently in the machine.
     *
     * @return an {@link Optional} containing the current {@link Pet}, or an empty {@link Optional} if no pet is present
     */
    public Optional<Pet> getPet() {
        return Optional.ofNullable(this.pet);
    }

    /**
     * Checks whether a pet is currently placed in the machine.
     *
     * @return {@code true} if a pet is present, {@code false} otherwise
     */
    public boolean hasPet() {
        return getPet().isPresent();
    }

    /**
     * Places a pet into the bathing machine.
     *
     * @param pet the pet to place in the machine
     * @throws MachineIsDirtyException           if the machine has not been cleaned
     * @throws PetIsAlreadyInMachineException    if another pet is already in the machine
     */
    public void putPet(Pet pet) {
        if (!this.clean) throw new MachineIsDirtyException();
        if (hasPet()) throw new PetIsAlreadyInMachineException(String.format("O pet %s está na máquina!", this.pet.getName()));
        this.pet = pet;
    }

    /**
     * Removes the pet from the bathing machine.
     * If the pet was removed while still dirty, the machine is marked as dirty.
     *
     * @throws PetNotInMachineException if there is no pet in the machine to remove
     */
    public Pet removePet() {
        if (!hasPet()) throw new PetNotInMachineException("A máquina está vazia.");
        if (!this.pet.isClean()) this.clean = false;

        Pet removedPet = this.pet;
        this.pet = null;
        return removedPet;
    }

    /**
     * Washes and sanitizes the machine itself.
     * Consumes 3.0 units of water and 1.0 unit of shampoo, setting the machine status to clean.
     *
     * @throws MachineIsCleanException if the machine is already clean
     * @throws InsufficientStockException if there is not enough water or shampoo
     */
    public void wash() {
        if (this.clean) throw new MachineIsCleanException();
        if (this.water < 3.0) throw new InsufficientStockException("água");
        if (this.shampoo < 1.0) throw new InsufficientStockException("shampoo");
        this.water -= 3.0;
        this.shampoo -= 1.0;
        this.clean = true;
    }
}
