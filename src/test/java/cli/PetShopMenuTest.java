package cli;

import com.samuel.model.PetBathingMachine;
import com.samuel.cli.PetShopMenu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Unit tests for PetShopMenu class")
class PetShopMenuTest {

    private PetBathingMachine machine;
    private ByteArrayOutputStream outputBuffer;
    private PrintStream out;

    @BeforeEach
    void setUp() {
        machine = new PetBathingMachine();
        outputBuffer = new ByteArrayOutputStream();
        out = new PrintStream(outputBuffer, true, StandardCharsets.UTF_8);
    }

    /**
     * Helper to build a {@link PetShopMenu} with a given sequence of input lines,
     * always appending a "0" (exit) command at the end so the loop terminates.
     */
    private PetShopMenu menuWith(String... inputLines) {
        // Join all lines + the mandatory exit "0" with newlines
        StringBuilder sb = new StringBuilder();
        for (String line : inputLines) {
            sb.append(line).append("\n");
        }
        sb.append("0\n"); // exit
        sb.append("\n");  // pause after exit (not consumed because option == 0)
        InputStream in = new ByteArrayInputStream(sb.toString().getBytes(StandardCharsets.UTF_8));
        return new PetShopMenu(in, out, machine);
    }

    /** Returns the full text printed to the output during the test. */
    private String output() {
        return outputBuffer.toString(StandardCharsets.UTF_8);
    }

    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Option 0 – exit")
    class ExitTests {

        @Test
        @DisplayName("Should print exit message and terminate the loop")
        void shouldPrintExitMessageAndTerminate() {
            menuWith().init(); // only "0\n" is in the stream
            assertThat(output())
                    .contains("Saindo...")
                    .contains("Saindo do sistema do PetShop...");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Option 1 – take a shower in pet")
    class TakeShowerTests {

        @Test
        @DisplayName("Should print success message with pet name after bathing")
        void shouldBathePetAndPrintSuccess() {
            machine.putPet(new com.samuel.model.Pet("Rex"));
            menuWith("1", "").init(); // "1" then Enter (pause), then "0"
            assertThat(output()).contains("Banho realizado em Rex com sucesso!");
        }

        @Test
        @DisplayName("Should print business error when no pet is in the machine")
        void shouldPrintBusinessErrorWhenNoPet() {
            menuWith("1", "").init();
            assertThat(output()).contains("[Erro de Negócio]:");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Option 2 – add water")
    class AddWaterTests {

        @Test
        @DisplayName("Should print success message when water is added")
        void shouldAddWaterAndPrintSuccess() {
            // consume some water first so we can add more
            machine.putPet(new com.samuel.model.Pet("Buddy"));
            machine.takeAShowerInPet();
            machine.removePet();

            menuWith("2", "").init();
            assertThat(output()).contains("Água abastecida com sucesso!");
        }

        @Test
        @DisplayName("Should print business error when water is already at maximum")
        void shouldPrintBusinessErrorWhenWaterAtMax() {
            // machine starts full at 30.0
            menuWith("2", "").init();
            assertThat(output()).contains("[Erro de Negócio]:");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Option 3 – add shampoo")
    class AddShampooTests {

        @Test
        @DisplayName("Should print success message when shampoo is added")
        void shouldAddShampooAndPrintSuccess() {
            machine.putPet(new com.samuel.model.Pet("Bella"));
            machine.takeAShowerInPet();
            machine.removePet();

            menuWith("3", "").init();
            assertThat(output()).contains("Shampoo abastecido com sucesso!");
        }

        @Test
        @DisplayName("Should print business error when shampoo is already at maximum")
        void shouldPrintBusinessErrorWhenShampooAtMax() {
            menuWith("3", "").init();
            assertThat(output()).contains("[Erro de Negócio]:");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Option 4 – check water level")
    class WaterLevelTests {

        @Test
        @DisplayName("Should print current water level")
        void shouldPrintWaterLevel() {
            menuWith("4", "").init();
            assertThat(output()).contains("Nível da água:").contains("/ 30L");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Option 5 – check shampoo level")
    class ShampooLevelTests {

        @Test
        @DisplayName("Should print current shampoo level")
        void shouldPrintShampooLevel() {
            menuWith("5", "").init();
            assertThat(output()).contains("Nível do shampoo:").contains("/ 10L");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Option 6 – check if pet is in machine")
    class CheckPetTests {

        @Test
        @DisplayName("Should report that no pet is in the machine")
        void shouldReportNoPet() {
            menuWith("6", "").init();
            assertThat(output()).contains("Não há pet na máquina.");
        }

        @Test
        @DisplayName("Should report that a pet is in the machine")
        void shouldReportPetPresent() {
            machine.putPet(new com.samuel.model.Pet("Max"));
            menuWith("6", "").init();
            assertThat(output()).contains("Sim, tem um pet na máquina.");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Option 7 – put pet in machine")
    class PutPetTests {

        @Test
        @DisplayName("Should put pet in machine and print success message")
        void shouldPutPetAndPrintSuccess() {
            menuWith("7", "Luna", "").init(); // option 7, pet name, Enter (pause)
            assertThat(output()).contains("Luna foi colocado na máquina.");
        }

        @Test
        @DisplayName("Should reject a blank pet name")
        void shouldRejectBlankPetName() {
            menuWith("7", "   ", "").init();
            assertThat(output()).contains("O nome do pet não pode estar em branco ou ter menos de 2 caracteres!");
        }

        @Test
        @DisplayName("Should reject a pet name shorter than 2 characters")
        void shouldRejectShortPetName() {
            menuWith("7", "A", "").init();
            assertThat(output()).contains("O nome do pet não pode estar em branco ou ter menos de 2 caracteres!");
        }

        @Test
        @DisplayName("Should print business error when machine already has a pet")
        void shouldPrintBusinessErrorWhenMachineHasPet() {
            machine.putPet(new com.samuel.model.Pet("Spike"));
            menuWith("7", "Rocky", "").init();
            assertThat(output()).contains("[Erro de Negócio]:");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Option 8 – remove pet from machine")
    class RemovePetTests {

        @Test
        @DisplayName("Should remove pet and print success message")
        void shouldRemovePetAndPrintSuccess() {
            machine.putPet(new com.samuel.model.Pet("Toby"));
            menuWith("8", "").init();
            assertThat(output()).contains("Toby foi retirado da máquina.");
        }

        @Test
        @DisplayName("Should print business error when machine is empty")
        void shouldPrintBusinessErrorWhenMachineEmpty() {
            menuWith("8", "").init();
            assertThat(output()).contains("[Erro de Negócio]:");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Option 9 – wash machine")
    class WashMachineTests {

        @Test
        @DisplayName("Should wash dirty machine and print success message")
        void shouldWashMachineAndPrintSuccess() {
            // Make machine dirty
            machine.putPet(new com.samuel.model.Pet("Dirty"));
            machine.removePet();

            menuWith("9", "").init();
            assertThat(output()).contains("Máquina limpa com sucesso!");
        }

        @Test
        @DisplayName("Should print business error when machine is already clean")
        void shouldPrintBusinessErrorWhenMachineAlreadyClean() {
            menuWith("9", "").init();
            assertThat(output()).contains("[Erro de Negócio]:");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Invalid input handling")
    class InvalidInputTests {

        @Test
        @DisplayName("Should print invalid value message for non-numeric input")
        void shouldPrintInvalidMessageForNonNumericInput() {
            menuWith("abc", "").init();
            assertThat(output()).contains("Valor inválido! Digite um número inteiro.");
        }

        @Test
        @DisplayName("Should print invalid operation message for out-of-range option")
        void shouldPrintInvalidOperationForOutOfRangeOption() {
            menuWith("99", "").init();
            assertThat(output()).contains("Operação inválida!");
        }
    }
}
