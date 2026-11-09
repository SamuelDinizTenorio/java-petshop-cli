package com.samuel;

import com.samuel.cli.PetShopMenu;

/**
 * Application entry point for the Pet Shop CLI system.
 * <p>
 * Instantiates the {@link PetShopMenu} and starts the interactive
 * command-line interface loop.
 * </p>
 */
public class Main {

    /**
     * Main method that bootstraps the Pet Shop CLI application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        var menu = new PetShopMenu();
        menu.init();
    }
}
