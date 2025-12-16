package uz.wcaproject.generators;

import org.springframework.stereotype.Component;

import java.util.function.Predicate;

@Component
public class UserNameGenerator {

    public String generateUsername(String firstName, String lastName, Predicate<String> predicate) {
        String baseUsername = firstName + "." + lastName;
        String username = baseUsername;
        int counter = 1;

        while (predicate.test(username)) {
            username = baseUsername + counter;
            counter++;
        }

        return username;
    }
}
