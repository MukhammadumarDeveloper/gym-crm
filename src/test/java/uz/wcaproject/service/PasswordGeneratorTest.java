package uz.wcaproject.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uz.wcaproject.generators.PasswordGenerator;

import static org.junit.jupiter.api.Assertions.*;

class PasswordGeneratorTest {

    private PasswordGenerator passwordGenerator;

    @BeforeEach
    void setUp() {
        passwordGenerator = new PasswordGenerator();
    }

    @Test
    void generatePassword_notNull() {
        String password = passwordGenerator.generatePassword();
        assertNotNull(password, "Generated password should not be null");
    }

    @Test
    void generatePassword_correctLength() {
        String password = passwordGenerator.generatePassword();
        assertEquals(10, password.length(), "Password should be 10 characters long");
    }

    @Test
    void generatePassword_onlyAllowedCharacters() {
        String password = passwordGenerator.generatePassword();
        String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        for (char c : password.toCharArray()) {
            assertTrue(allowedChars.indexOf(c) >= 0,
                    "Password contains invalid character: " + c);
        }
    }

    @Test
    void generatePassword_randomness() {
        String pass1 = passwordGenerator.generatePassword();
        String pass2 = passwordGenerator.generatePassword();
        // Very small chance they are equal, but generally ok for test
        assertNotEquals(pass1, pass2, "Two consecutive passwords should not be identical");
    }
}
