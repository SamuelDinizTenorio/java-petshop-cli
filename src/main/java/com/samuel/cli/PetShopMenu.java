package com.samuel.cli;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

import com.samuel.exception.BusinessException;
import com.samuel.model.Pet;
import com.samuel.model.PetBathingMachine;

/**
 * Command-line interface menu for interacting with the pet shop bathing machine system.
 * <p>
 * Presents an interactive text menu to the user and dispatches the chosen operations
 * to the underlying {@link PetBathingMachine}. Accepts injectable {@link InputStream}
 * and {@link PrintStream} dependencies to facilitate unit testing.
 * </p>
 */
public class PetShopMenu {

    private final Scanner scanner;
    private final PetBathingMachine machine;
    private final PrintStream out;

    /**
     * Creates a {@code PetShopMenu} using {@link System#in} and {@link System#out}
     * as the default I/O streams, and a new {@link PetBathingMachine} instance.
     */
    public PetShopMenu() {
        this(System.in, System.out, new PetBathingMachine());
    }

    /**
     * Creates a {@code PetShopMenu} with injectable I/O streams and a machine instance.
     * <p>
     * This constructor is intended primarily for testing purposes.
     * </p>
     *
     * @param in      the input stream to read user commands from
     * @param out     the output stream to print messages to
     * @param machine the {@link PetBathingMachine} to operate on
     */
    public PetShopMenu(InputStream in, PrintStream out, PetBathingMachine machine) {
        this.scanner = new Scanner(in);
        this.out = out;
        this.machine = machine;
    }

    /**
     * Starts the interactive menu loop.
     * <p>
     * Repeatedly shows the menu, reads user input, and dispatches the chosen option
     * until the user selects option {@code 0} (exit).
     * Invalid numeric input prints an error message; business-rule violations
     * are caught and printed as "{@code [Erro de Negócio]: <message>}".
     * </p>
     */
    public void init() {
        int option = -1;
        do {
            showMenu();
            try {
                out.print("Digite a operação: ");
                option = Integer.parseInt(scanner.nextLine().trim());
                executeOption(option);
            } catch (NumberFormatException e) {
                out.println("Valor inválido! Digite um número inteiro.");
            } catch (BusinessException e) {
                out.println("[Erro de Negócio]: " + e.getMessage());
            } catch (RuntimeException e) {
                out.println("[Erro Inesperado]: " + e.getMessage());
            }

            if (option != 0) {
                pause();
            }
        } while (option != 0);

        scanner.close();
        out.println("Saindo do sistema do PetShop...");
    }

    /**
     * Prints the main menu options to the output stream.
     */
    private void showMenu() {
        out.println("""
                    ================================================
                    ===          Escolha uma das opções          ===
                    ================================================
                    0 - Sair.
                    1 - Dar banho no pet. (Água -10L) (Shampoo -2L)
                    2 - Abastecer com água (+2L).
                    3 - Abastecer com shampoo (+2L).
                    4 - Verificar nível de água.
                    5 - Verificar nível de shampoo.
                    6 - Verificar se tem pet no banho.
                    7 - Colocar pet na máquina.
                    8 - Retirar pet da máquina.
                    9 - Limpar máquina. (Água -3L) (Shampoo -1L)
                    ================================================
                """);
    }

    /**
     * Executes the action corresponding to the chosen menu option.
     *
     * @param option the menu option selected by the user (0–9)
     */
    private void executeOption(int option) {
        switch (option) {
            case 0 -> out.println("Saindo...");
            case 1 -> {
                String petName = machine.getPet()
                        .map(Pet::getName)
                        .orElse("pet");
                machine.takeAShowerInPet();
                out.printf("Banho realizado em %s com sucesso!\n", petName);
            }
            case 2 -> {
                machine.addWater();
                out.println("Água abastecida com sucesso!");
            }
            case 3 -> {
                machine.addShampoo();
                out.println("Shampoo abastecido com sucesso!");
            }
            case 4 -> out.printf("Nível da água: %sL / 30L\n", machine.getWater());
            case 5 -> out.printf("Nível do shampoo: %sL / 10L\n", machine.getShampoo());
            case 6 -> out.println(machine.hasPet() ? "Sim, tem um pet na máquina." : "Não há pet na máquina.");
            case 7 -> {
                out.print("Digite o nome do pet: ");
                String petName = scanner.nextLine().trim();

                if (petName.isBlank() || petName.length() < 2) {
                    out.println("O nome do pet não pode estar em branco ou ter menos de 2 caracteres!");
                    break;
                }

                var pet = new Pet(petName);
                machine.putPet(pet);
                out.printf("\n%s foi colocado na máquina.\n", pet.getName());
            }
            case 8 -> {
                Pet removedPet = machine.removePet();
                out.printf("%s foi retirado da máquina.\n", removedPet.getName());
            }
            case 9 -> {
                machine.wash();
                out.println("Máquina limpa com sucesso!");
            }
            default -> out.println("Operação inválida!");
        }
    }

    /**
     * Waits for the user to press Enter before continuing.
     * Prints a prompt message and reads one line from the input stream.
     */
    private void pause() {
        out.println("\nPressione Enter para continuar...");
        scanner.nextLine();
    }
}
