package uz.wcaproject.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.wcaproject.dao.TraineeDAO;
import uz.wcaproject.generators.PasswordGenerator;
import uz.wcaproject.generators.UserNameGenerator;
import uz.wcaproject.model.Trainee;

import java.util.List;
import java.util.Optional;

@Service
public class TraineeService {
    private static final Logger logger = LoggerFactory.getLogger(TraineeService.class);

    private final TraineeDAO traineeDAO;

    private final PasswordGenerator passwordGenerator;

    private final UserNameGenerator userNameGenerator;

    @Autowired
    public TraineeService(TraineeDAO traineeDAO, UserNameGenerator userNameGenerator, PasswordGenerator passwordGenerator) {
        this.traineeDAO = traineeDAO;
        this.userNameGenerator = userNameGenerator;
        this.passwordGenerator = passwordGenerator;
    }

    public Trainee createTrainee(Trainee trainee) {
        logger.info("Creating new trainee profile for: {} {}", trainee.getFirstName(), trainee.getLastName());


        String username = userNameGenerator.generateUsername(
                trainee.getFirstName(),
                trainee.getLastName(),
                (name) -> traineeDAO.findByUsername(name).isPresent());
        String password = passwordGenerator.generatePassword();

        trainee.setUsername(username);
        trainee.setPassword(password);
        trainee.setIsActive(true);

        Trainee created = traineeDAO.create(trainee);
        logger.info("Trainee profile created successfully with username: {}", username);
        return created;
    }

    public Trainee updateTrainee(Trainee trainee) {
        logger.info("Updating trainee profile with id: {}", trainee.getId());

        Trainee existing = traineeDAO.findByUsername(trainee.getUsername())
                .orElseThrow(() -> {
                    logger.error("Trainee not found with id: {}", trainee.getId());
                    return new IllegalArgumentException("Trainee not found");
                });
        existing.setFirstName(trainee.getFirstName());
        existing.setLastName(trainee.getLastName());
        existing.setDateOfBirth(trainee.getDateOfBirth());
        existing.setAddress(trainee.getAddress());
        existing.setIsActive(trainee.getIsActive());

        Trainee updated = traineeDAO.update(existing);
        logger.info("Trainee profile updated successfully");
        return updated;
    }

    public void deleteTrainee(String username) {
        logger.info("Deleting trainee profile with username: {}", username);

        traineeDAO.delete(username);
        logger.info("Trainee profile deleted successfully");
    }

    public Optional<Trainee> getTraineeById(String id) {
        logger.debug("Retrieving trainee by id: {}", id);
        return traineeDAO.findById(id);
    }

    public Optional<Trainee> getTraineeByUsername(String username) {
        logger.debug("Retrieving trainee by username: {}", username);
        return traineeDAO.findByUsername(username);
    }

    public List<Trainee> getAllTrainees() {
        logger.debug("Retrieving all trainees");
        return traineeDAO.findAll();
    }
}