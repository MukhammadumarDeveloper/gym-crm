package uz.wcaproject.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uz.wcaproject.generators.UserNameGenerator;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class UserNameGeneratorTest {

    private UserNameGenerator userNameGenerator;

    @BeforeEach
    void setUp() {
        userNameGenerator = new UserNameGenerator();
    }

    @Test
    void generateUsername_uniqueNoCollision() {
        // Predicate always returns false → no collision
        Predicate<String> predicate = username -> false;

        String username = userNameGenerator.generateUsername("John", "Doe", predicate);

        assertEquals("John.Doe", username, "Username should match firstName.lastName");
    }

    @Test
    void generateUsername_singleCollision() {
        // Simulate a single collision on "John.Doe"
        Set<String> existing = new HashSet<>();
        existing.add("John.Doe");

        Predicate<String> predicate = existing::contains;

        String username = userNameGenerator.generateUsername("John", "Doe", predicate);

        assertEquals("John.Doe1", username, "Username should append 1 after base if first collision occurs");
    }

    @Test
    void generateUsername_multipleCollisions() {
        // Simulate multiple collisions
        Set<String> existing = new HashSet<>();
        existing.add("John.Doe");
        existing.add("John.Doe1");
        existing.add("John.Doe2");

        Predicate<String> predicate = existing::contains;

        String username = userNameGenerator.generateUsername("John", "Doe", predicate);

        assertEquals("John.Doe3", username, "Username should increment number until unique");
    }

    @Test
    void generateUsername_noSideEffects() {
        Predicate<String> predicate = username -> false;
        String username1 = userNameGenerator.generateUsername("Alice", "Smith", predicate);
        String username2 = userNameGenerator.generateUsername("Alice", "Smith", predicate);

        assertEquals("Alice.Smith", username1);
        assertEquals("Alice.Smith", username2, "Generator should not keep state between calls");
    }
}
