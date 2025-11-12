package uz.wcaproject.dao;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import uz.wcaproject.model.Trainee;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class TraineeDAO {
    private static final Logger logger = LoggerFactory.getLogger(TraineeDAO.class);
    private final AtomicLong idGenerator = new AtomicLong(1000);

    private Map<Long, Object> storage;

    @Autowired
    public void setStorage(@Qualifier("traineeStorage") Map<Long, Object> storage) {
        this.storage = storage;
    }

    public Trainee create(Trainee trainee) {
        Long id = idGenerator.incrementAndGet();
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

    public void delete(Long id) {
        if (storage.remove(id) != null) {
            logger.info("Deleted trainee with id: {}", id);
        } else {
            logger.warn("Attempted to delete non-existent trainee with id: {}", id);
        }
    }

    public Optional<Trainee> findById(Long id) {
        Trainee trainee = (Trainee) storage.get(id);
        logger.debug("Finding trainee by id: {}, found: {}", id, trainee != null);
        return Optional.ofNullable(trainee);
    }

    public List<Trainee> findAll() {
        List<Trainee> trainees = storage.values().stream()
                .map(obj -> (Trainee) obj)
                .collect(Collectors.toList());
        logger.debug("Finding all trainees, count: {}", trainees.size());
        return trainees;
    }

    public Optional<Trainee> findByUsername(String username) {
        Optional<Trainee> trainee = storage.values().stream()
                .map(obj -> (Trainee) obj)
                .filter(t -> t.getUser() != null && username.equals(t.getUser().getUsername()))
                .findFirst();
        logger.debug("Finding trainee by username: {}, found: {}", username, trainee.isPresent());
        return trainee;
    }
}