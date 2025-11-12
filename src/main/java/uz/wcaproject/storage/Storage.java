package uz.wcaproject.storage;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import uz.wcaproject.model.Trainee;
import uz.wcaproject.model.Trainer;
import uz.wcaproject.model.TrainingType;
import uz.wcaproject.model.User;

import javax.annotation.PostConstruct;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.Map;
import java.util.Random;
@Component
public class Storage {
    private static final Logger logger = LoggerFactory.getLogger(Storage.class);
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int PASSWORD_LENGTH = 10;
    private final Random random = new Random();

    private final String initFilePath;

    @Autowired
    @Qualifier("traineeStorage")
    private Map<Long, Object> traineeStorage;

    @Autowired
    @Qualifier("trainerStorage")
    private Map<Long, Object> trainerStorage;

    @Autowired
    @Qualifier("trainingStorage")
    private Map<Long, Object> trainingStorage;

    @Autowired
    @Qualifier("trainingTypeStorage")
    private Map<Long, Object> trainingTypeStorage;

    @Autowired
    @Qualifier("userStorage")
    private Map<String, Object> userStorage;

    public Storage(String initFilePath) {
        this.initFilePath = initFilePath;
    }

    @PostConstruct
    public void initializeStorage() {
        logger.info("Starting storage initialization from file: {}", initFilePath);

        try {
            String fileName = initFilePath.replace("classpath:", "");
            ClassPathResource resource = new ClassPathResource(fileName);

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty() || line.startsWith("#")) {
                        continue;
                    }
                    processLine(line);
                }
            }

            logger.info("Storage initialization completed successfully");
            logger.info("Loaded {} training types, {} trainers, {} trainees",
                    trainingTypeStorage.size(), trainerStorage.size(), traineeStorage.size());
        } catch (IOException e) {
            logger.error("Error initializing storage from file: {}", initFilePath, e);
        }
    }

    private void processLine(String line) {
        String[] parts = line.split(":");
        if (parts.length < 2) {
            logger.warn("Invalid line format: {}", line);
            return;
        }

        String type = parts[0];

        try {
            switch (type) {
                case "TRAINING_TYPE":
                    loadTrainingType(parts);
                    break;
                case "TRAINER":
                    loadTrainer(parts);
                    break;
                case "TRAINEE":
                    loadTrainee(parts);
                    break;
                default:
                    logger.warn("Unknown type: {}", type);
            }
        } catch (Exception e) {
            logger.error("Error processing line: {}", line, e);
        }
    }

    private void loadTrainingType(String[] parts) {
        if (parts.length < 3) return;

        Long id = Long.parseLong(parts[1]);
        String name = parts[2];

        TrainingType trainingType = new TrainingType(id, name);
        trainingTypeStorage.put(id, trainingType);
        logger.debug("Loaded training type: {}", trainingType);
    }

    private void loadTrainer(String[] parts) {
        if (parts.length < 5) return;

        Long id = Long.parseLong(parts[1]);
        String firstName = parts[2];
        String lastName = parts[3];
        String specialization = parts[4];

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(generateUsername(firstName, lastName));
        user.setPassword(generatePassword());
        user.setIsActive(true);

        userStorage.put(user.getUsername(), user);

        Trainer trainer = new Trainer(id, specialization, user);
        trainerStorage.put(id, trainer);
        logger.debug("Loaded trainer: {}", trainer);
    }

    private void loadTrainee(String[] parts) {
        if (parts.length < 6) return;

        Long id = Long.parseLong(parts[1]);
        String firstName = parts[2];
        String lastName = parts[3];
        LocalDate dateOfBirth = LocalDate.parse(parts[4]);
        String address = parts[5];

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(generateUsername(firstName, lastName));
        user.setPassword(generatePassword());
        user.setIsActive(true);

        userStorage.put(user.getUsername(), user);

        Trainee trainee = new Trainee(id, dateOfBirth, address, user);
        traineeStorage.put(id, trainee);
        logger.debug("Loaded trainee: {}", trainee);
    }

    public String generateUsername(String firstName, String lastName) {
        String baseUsername = firstName + "." + lastName;
        String username = baseUsername;
        int counter = 1;

        while (userStorage.containsKey(username)) {
            username = baseUsername + counter;
            counter++;
        }

        logger.debug("Generated username: {}", username);
        return username;
    }

    public String generatePassword() {
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            password.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        String generatedPassword = password.toString();
        logger.debug("Generated password with length: {}", PASSWORD_LENGTH);
        return generatedPassword;
    }

    public Map<Long, Object> getTraineeStorage() {
        return traineeStorage;
    }

    public Map<Long, Object> getTrainerStorage() {
        return trainerStorage;
    }

    public Map<Long, Object> getTrainingStorage() {
        return trainingStorage;
    }

    public Map<Long, Object> getTrainingTypeStorage() {
        return trainingTypeStorage;
    }

    public Map<String, Object> getUserStorage() {
        return userStorage;
    }
}