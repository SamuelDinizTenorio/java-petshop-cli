package model;

import com.samuel.exception.PetIsAlreadyCleanException;
import com.samuel.model.Pet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Unit tests for Pet class")
class PetTest {

    @Nested
    @DisplayName("Initialization and initial state")
    class InitializationTests {

        @Test
        @DisplayName("Should initialize pet with the given name and dirty status (clean = false)")
        void shouldInitializePetCorrectly() {
            Pet pet = new Pet("Rex");

            assertThat(pet.getName()).isEqualTo("Rex");
            assertThat(pet.isClean()).isFalse();
        }
    }

    @Nested
    @DisplayName("clean() method")
    class CleanTests {

        @Test
        @DisplayName("Should clean pet successfully when it is dirty")
        void shouldCleanPetWhenPetIsDirty() {
            Pet pet = new Pet("Mel");

            pet.clean();

            assertThat(pet.isClean()).isTrue();
        }

        @Test
        @DisplayName("Should throw PetIsAlreadyCleanException when pet is already clean")
        void shouldThrowExceptionWhenPetIsAlreadyClean() {
            Pet pet = new Pet("Thor");
            pet.clean();

            assertThatThrownBy(pet::clean)
                    .isInstanceOf(PetIsAlreadyCleanException.class)
                    .hasMessage("O pet Thor já está limpo.");
        }
    }
}
