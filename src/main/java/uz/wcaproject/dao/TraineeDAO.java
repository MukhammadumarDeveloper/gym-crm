package uz.wcaproject.dao;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import uz.wcaproject.model.Trainee;

import java.util.*;

@Repository
public class TraineeDAO {
    private static final Logger logger = LoggerFactory.getLogger(TraineeDAO.class);

    private final Map<UUID, Trainee> storage;

    @Autowired
    public TraineeDAO(@Qualifier("traineeStorage") Map<UUID, Trainee> storage) {
        this.storage = storage;
    }

    public Trainee create(Trainee trainee) {
        UUID id = UUID.randomUUID();
        trainee.setId(id);
        storage.put(id, trainee);
        logger.info("Created trainee with id: {}", id);
        return trainee;
    }

    public Trainee update(Trainee trainee) {
        if (trainee.getId() == null || !storage.containsKey(trainee.getId())) {
            logger.error("Attempted to update non-existent trainee with id: {}", trainee.getId());
            throw new IllegalArgumentException("Trainee not found");
        }
        storage.put(trainee.getId(), trainee);
        logger.info("Updated trainee with id: {}", trainee.getId());
        return trainee;
    }

    public void delete(String username) {

        Trainee trainee = findByUsername(username).orElse(null);
        if (trainee != null) {
            storage.remove(trainee.getId());
            logger.info("Deleted trainee with username: {}", username);

        } else {
            logger.warn("Attempted to delete non-existent trainee with username: {}", username);
        }
    }

    public Optional<Trainee> findById(String id) {
        Trainee trainee = storage.get(UUID.fromString(id));
        logger.debug("Finding trainee by id: {}, found: {}", id, trainee != null);
        return Optional.ofNullable(trainee);
    }

    public List<Trainee> findAll() {
        List<Trainee> trainees = storage.values().stream()
                .toList();
        logger.debug("Finding all trainees, count: {}", trainees.size());
        return trainees;
    }

    public Optional<Trainee> findByUsername(String username) {
        Optional<Trainee> trainee = storage.values().stream()
                .filter(td -> td.getUsername().equals(username))
                .findFirst();
        logger.debug("Finding trainee by username: {}, found: {}", username, trainee.isPresent());
        return trainee;
    }
}