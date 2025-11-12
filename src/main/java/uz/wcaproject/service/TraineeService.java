package uz.wcaproject.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.wcaproject.dao.TraineeDAO;
import uz.wcaproject.model.Trainee;
import uz.wcaproject.model.User;
import uz.wcaproject.storage.Storage;

import java.util.List;
import java.util.Optional;

@Service
public class TraineeService {
    private static final Logger logger = LoggerFactory.getLogger(TraineeService.class);

    private TraineeDAO traineeDAO;
    private Storage storage;

    @Autowired
    public void setTraineeDAO(TraineeDAO traineeDAO) {
        this.traineeDAO = traineeDAO;
    }

    @Autowired
    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public Trainee createTrainee(Trainee trainee) {
        logger.info("Creating new trainee profile for: {} {}",
                trainee.getUser().getFirstName(), trainee.getUser().getLastName());

        User user = trainee.getUser();
        String username = storage.generateUsername(user.getFirstName(), user.getLastName());
        String password = storage.generatePassword();

        user.setUsername(username);
        user.setPassword(password);
        user.setIsActive(true);

        storage.getUserStorage().put(username, user);

        Trainee created = traineeDAO.create(trainee);
        logger.info("Trainee profile created successfully with username: {}", username);
        return created;
    }

    public Trainee updateTrainee(Trainee trainee) {
        logger.info("Updating trainee profile with id: {}", trainee.getId());

        Optional<Trainee> existing = traineeDAO.findById(trainee.getId());
        if (!existing.isPresent()) {
            logger.error("Trainee not found with id: {}", trainee.getId());
            throw new IllegalArgumentException("Trainee not found");
        }

        Trainee updated = traineeDAO.update(trainee);
        logger.info("Trainee profile updated successfully");
        return updated;
    }

    public void deleteTrainee(Long id) {
        logger.info("Deleting trainee profile with id: {}", id);

        Optional<Trainee> trainee = traineeDAO.findById(id);
        if (trainee.isPresent() && trainee.get().getUser() != null) {
            storage.getUserStorage().remove(trainee.get().getUser().getUsername());
        }

        traineeDAO.delete(id);
        logger.info("Trainee profile deleted successfully");
    }

    public Optional<Trainee> getTraineeById(Long id) {
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