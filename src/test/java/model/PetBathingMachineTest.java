package model;

import com.samuel.exception.InsufficientStockException;
import com.samuel.exception.MachineIsCleanException;
import com.samuel.exception.MachineIsDirtyException;
import com.samuel.exception.MaximumCapacityReachedException;
import com.samuel.exception.PetIsAlreadyInMachineException;
import com.samuel.exception.PetNotInMachineException;
import com.samuel.model.Pet;
import com.samuel.model.PetBathingMachine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Unit tests for PetBathingMachine class")
class PetBathingMachineTest {

    private PetBathingMachine machine;

    @BeforeEach
    void setUp() {
        machine = new PetBathingMachine();
    }

    @Nested
    @DisplayName("Initialization and initial state")
    class InitializationTests {

        @Test
        @DisplayName("Should initialize machine with 30.0 water, 10.0 shampoo, clean, and without pet")
        void shouldInitializeMachineWithDefaultValues() {
            assertThat(machine.getWater()).isEqualTo(30.0);
            assertThat(machine.getShampoo()).isEqualTo(10.0);
            assertThat(machine.hasPet()).isFalse();
        }
    }

    @Nested
    @DisplayName("putPet() method")
    class PutPetTests {

        @Test
        @DisplayName("Should place pet in machine successfully when machine is clean and empty")
        void shouldPutPetSuccessfully() {
            Pet pet = new Pet("Buddy");

            machine.putPet(pet);

            assertThat(machine.hasPet()).isTrue();
        }

        @Test
        @DisplayName("Should throw MachineIsDirtyException when placing pet in a dirty machine")
        void shouldThrowExceptionWhenMachineIsDirty() {
            Pet dirtyPet = new Pet("Dirty Dog");
            machine.putPet(dirtyPet);
            machine.removePet(); // Removes without cleaning, making machine dirty

            Pet nextPet = new Pet("Next Dog");

            assertThatThrownBy(() -> machine.putPet(nextPet))
                    .isInstanceOf(MachineIsDirtyException.class)
                    .hasMessage("A máquina está suja, para colocar o pet é necessário limpa-la!");
        }

        @Test
        @DisplayName("Should throw PetIsAlreadyInMachineException when another pet is already in machine")
        void shouldThrowExceptionWhenPetAlreadyInMachine() {
            Pet firstPet = new Pet("Max");
            Pet secondPet = new Pet("Bella");
            machine.putPet(firstPet);

            assertThatThrownBy(() -> machine.putPet(secondPet))
                    .isInstanceOf(PetIsAlreadyInMachineException.class)
                    .hasMessage("O pet Max está na máquina!");
        }
    }

    @Nested
    @DisplayName("removePet() method")
    class RemovePetTests {

        @Test
        @DisplayName("Should throw PetNotInMachineException when trying to remove pet from empty machine")
        void shouldThrowExceptionWhenMachineIsEmpty() {
            assertThatThrownBy(() -> machine.removePet())
                    .isInstanceOf(PetNotInMachineException.class)
                    .hasMessage("A máquina está vazia.");
        }

        @Test
        @DisplayName("Should remove clean pet and keep machine clean")
        void shouldRemoveCleanPetAndKeepMachineClean() {
            Pet pet = new Pet("Rex");
            machine.putPet(pet);
            machine.takeAShowerInPet();

            machine.removePet();

            assertThat(machine.hasPet()).isFalse();
            // Verify machine remains clean by successfully putting another pet
            Pet nextPet = new Pet("Spike");
            machine.putPet(nextPet);
            assertThat(machine.hasPet()).isTrue();
        }

        @Test
        @DisplayName("Should remove dirty pet and mark machine as dirty")
        void shouldRemoveDirtyPetAndMarkMachineDirty() {
            Pet pet = new Pet("Rex");
            machine.putPet(pet);

            machine.removePet();

            assertThat(machine.hasPet()).isFalse();
            // Machine is dirty; putting another pet should throw MachineIsDirtyException
            Pet nextPet = new Pet("Spike");
            assertThatThrownBy(() -> machine.putPet(nextPet))
                    .isInstanceOf(MachineIsDirtyException.class);
        }
    }

    @Nested
    @DisplayName("takeAShowerInPet() method")
    class TakeAShowerInPetTests {

        @Test
        @DisplayName("Should bathe pet successfully, consuming 10.0 water, 2.0 shampoo, and cleaning pet")
        void shouldBathePetSuccessfully() {
            Pet pet = new Pet("Luna");
            machine.putPet(pet);

            machine.takeAShowerInPet();

            assertThat(pet.isClean()).isTrue();
            assertThat(machine.getWater()).isEqualTo(20.0);
            assertThat(machine.getShampoo()).isEqualTo(8.0);
        }

        @Test
        @DisplayName("Should throw PetNotInMachineException when taking shower without a pet")
        void shouldThrowExceptionWhenNoPetInMachine() {
            assertThatThrownBy(() -> machine.takeAShowerInPet())
                    .isInstanceOf(PetNotInMachineException.class)
                    .hasMessage("Coloque o pet na máquina para iniciar o banho");
        }

        @Test
        @DisplayName("Should throw InsufficientStockException when water is less than 10.0")
        void shouldThrowExceptionWhenWaterIsInsufficientForBath() {
            // Initial water: 30.0. Bath 1: 20.0. Bath 2: 10.0. Bath 3: 0.0.
            Pet pet1 = new Pet("Pet 1");
            machine.putPet(pet1);
            machine.takeAShowerInPet();
            machine.removePet();

            Pet pet2 = new Pet("Pet 2");
            machine.putPet(pet2);
            machine.takeAShowerInPet();
            machine.removePet();

            Pet pet3 = new Pet("Pet 3");
            machine.putPet(pet3);
            machine.takeAShowerInPet();
            machine.removePet();

            // Water is now 0.0, refill shampoo to ensure only water triggers exception
            machine.addShampoo();
            machine.addShampoo();

            Pet pet4 = new Pet("Pet 4");
            machine.putPet(pet4);

            assertThatThrownBy(() -> machine.takeAShowerInPet())
                    .isInstanceOf(InsufficientStockException.class)
                    .hasMessage("Estoque de água insuficiente! È necessario reabastecer.");
        }

        @Test
        @DisplayName("Should throw InsufficientStockException when shampoo is less than 2.0")
        void shouldThrowExceptionWhenShampooIsInsufficientForBath() {
            // Initial shampoo: 10.0, water: 30.0.
            // Each bath consumes 10.0 water and 2.0 shampoo.
            // After 3 baths: shampoo is 4.0, water is 0.0.
            for (int i = 1; i <= 3; i++) {
                Pet pet = new Pet("Pet " + i);
                machine.putPet(pet);
                machine.takeAShowerInPet();
                machine.removePet();
            }

            // Refill water (needs to be >= 10.0)
            for (int i = 0; i < 5; i++) {
                machine.addWater(); // 5 * 2.0 = 10.0 water
            }

            // 4th bath: water: 10.0 -> 0.0, shampoo: 4.0 -> 2.0
            Pet pet4 = new Pet("Pet 4");
            machine.putPet(pet4);
            machine.takeAShowerInPet();
            machine.removePet();

            // Refill water again (needs to be >= 10.0)
            for (int i = 0; i < 5; i++) {
                machine.addWater();
            }

            // 5th bath: water: 10.0 -> 0.0, shampoo: 2.0 -> 0.0
            Pet pet5 = new Pet("Pet 5");
            machine.putPet(pet5);
            machine.takeAShowerInPet();
            machine.removePet();

            // Refill water so water is plenty (>= 10.0), but shampoo is 0.0 (< 2.0)
            for (int i = 0; i < 5; i++) {
                machine.addWater();
            }

            Pet pet6 = new Pet("Pet 6");
            machine.putPet(pet6);

            assertThatThrownBy(() -> machine.takeAShowerInPet())
                    .isInstanceOf(InsufficientStockException.class)
                    .hasMessage("Estoque de shampoo insuficiente! È necessario reabastecer.");
        }
    }

    @Nested
    @DisplayName("Water management: getWater() and addWater()")
    class WaterManagementTests {

        @Test
        @DisplayName("Should increase water by 2.0 when adding water below maximum capacity")
        void shouldAddWaterSuccessfully() {
            Pet pet = new Pet("Rocky");
            machine.putPet(pet);
            machine.takeAShowerInPet(); // Water becomes 20.0

            machine.addWater(); // Water becomes 22.0

            assertThat(machine.getWater()).isEqualTo(22.0);
        }

        @Test
        @DisplayName("Should throw MaximumCapacityReachedException when water is already at 30.0")
        void shouldThrowExceptionWhenWaterAtMaxCapacity() {
            assertThatThrownBy(() -> machine.addWater())
                    .isInstanceOf(MaximumCapacityReachedException.class)
                    .hasMessage("Capacida máxima de água atingida!");
        }
    }

    @Nested
    @DisplayName("Shampoo management: getShampoo() and addShampoo()")
    class ShampooManagementTests {

        @Test
        @DisplayName("Should increase shampoo by 2.0 when adding shampoo below maximum capacity")
        void shouldAddShampooSuccessfully() {
            Pet pet = new Pet("Rocky");
            machine.putPet(pet);
            machine.takeAShowerInPet(); // Shampoo becomes 8.0

            machine.addShampoo(); // Shampoo becomes 10.0

            assertThat(machine.getShampoo()).isEqualTo(10.0);
        }

        @Test
        @DisplayName("Should throw MaximumCapacityReachedException when shampoo is already at 10.0")
        void shouldThrowExceptionWhenShampooAtMaxCapacity() {
            assertThatThrownBy(() -> machine.addShampoo())
                    .isInstanceOf(MaximumCapacityReachedException.class)
                    .hasMessage("Capacida máxima de shampoo atingida!");
        }
    }

    @Nested
    @DisplayName("wash() method")
    class WashMachineTests {

        @Test
        @DisplayName("Should throw MachineIsCleanException when trying to wash an already clean machine")
        void shouldThrowExceptionWhenMachineIsAlreadyClean() {
            assertThatThrownBy(() -> machine.wash())
                    .isInstanceOf(MachineIsCleanException.class)
                    .hasMessage("A máquina já está limpa.");
        }

        @Test
        @DisplayName("Should wash machine successfully, consuming 3.0 water, 1.0 shampoo, and setting clean to true")
        void shouldWashMachineSuccessfully() {
            // Make machine dirty
            Pet dirtyPet = new Pet("Dirty");
            machine.putPet(dirtyPet);
            machine.removePet();

            Double initialWater = machine.getWater();
            Double initialShampoo = machine.getShampoo();

            machine.wash();

            assertThat(machine.getWater()).isEqualTo(initialWater - 3.0);
            assertThat(machine.getShampoo()).isEqualTo(initialShampoo - 1.0);

            // Verify clean by successfully placing a pet without MachineIsDirtyException
            Pet nextPet = new Pet("Clean");
            machine.putPet(nextPet);
            assertThat(machine.hasPet()).isTrue();
        }

        @Test
        @DisplayName("Should throw InsufficientStockException when water is less than 3.0 during wash")
        void shouldThrowExceptionWhenWaterIsInsufficientForWash() {
            // Drain water by taking 3 showers (30 - 30 = 0.0)
            for (int i = 1; i <= 3; i++) {
                Pet pet = new Pet("Pet " + i);
                machine.putPet(pet);
                machine.takeAShowerInPet();
                machine.removePet();
            }

            // At this point water is 0.0, but the machine was kept clean because pets were cleaned before removal.
            // Put and remove an unwashed pet to make the machine dirty without needing water.
            Pet dirtyPet = new Pet("Dirty Pet");
            machine.putPet(dirtyPet);
            machine.removePet(); // Machine is now dirty!

            // Water is 0.0 (< 3.0), machine is dirty, shampoo is 4.0 (>= 1.0)
            assertThatThrownBy(() -> machine.wash())
                    .isInstanceOf(InsufficientStockException.class)
                    .hasMessage("Estoque de água insuficiente! È necessario reabastecer.");
        }

        @Test
        @DisplayName("Should throw InsufficientStockException when shampoo is less than 1.0 during wash")
        void shouldThrowExceptionWhenShampooIsInsufficientForWash() {
            // Drain shampoo by taking 5 showers (10 - 10 = 0.0)
            for (int i = 1; i <= 3; i++) {
                Pet pet = new Pet("Pet " + i);
                machine.putPet(pet);
                machine.takeAShowerInPet();
                machine.removePet();
            }
            for (int i = 0; i < 5; i++) machine.addWater();

            Pet pet4 = new Pet("Pet 4");
            machine.putPet(pet4);
            machine.takeAShowerInPet();
            machine.removePet();
            for (int i = 0; i < 5; i++) machine.addWater();

            Pet pet5 = new Pet("Pet 5");
            machine.putPet(pet5);
            machine.takeAShowerInPet();
            machine.removePet();

            // Refill water so water >= 3.0 (e.g. 10.0), shampoo remains 0.0 (< 1.0)
            for (int i = 0; i < 5; i++) machine.addWater();

            // Make machine dirty by putting and removing a dirty pet
            Pet dirtyPet = new Pet("Dirty Pet");
            machine.putPet(dirtyPet);
            machine.removePet(); // Machine is now dirty!

            // Shampoo is 0.0 (< 1.0), water is plentiful, machine is dirty
            assertThatThrownBy(() -> machine.wash())
                    .isInstanceOf(InsufficientStockException.class)
                    .hasMessage("Estoque de shampoo insuficiente! È necessario reabastecer.");
        }
    }
}
