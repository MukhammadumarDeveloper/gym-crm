package uz.wcaproject.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import uz.wcaproject.model.Training;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class TrainingDAO {
    private static final Logger logger = LoggerFactory.getLogger(TrainingDAO.class);
    private final AtomicLong idGenerator = new AtomicLong(1000);

    private Map<Long, Object> storage;

    @Autowired
    public void setStorage(@Qualifier("trainingStorage") Map<Long, Object> storage) {
        this.storage = storage;
    }

    public Training create(Training training) {
        Long id = idGenerator.incrementAndGet();
        training.setId(id);
        storage.put(id, training);
        logger.info("Created training with id: {}", id);
        return training;
    }

    public Optional<Training> findById(Long id) {
        Training training = (Training) storage.get(id);
        logger.debug("Finding training by id: {}, found: {}", id, training != null);
        return Optional.ofNullable(training);
    }

    public List<Training> findAll() {
        List<Training> trainings = storage.values().stream()
                .map(obj -> (Training) obj)
                .collect(Collectors.toList());
        logger.debug("Finding all trainings, count: {}", trainings.size());
        return trainings;
    }

    public List<Training> findByTraineeId(Long traineeId) {
        List<Training> trainings = storage.values().stream()
                .map(obj -> (Training) obj)
                .filter(t -> traineeId.equals(t.getTraineeId()))
                .collect(Collectors.toList());
        logger.debug("Finding trainings by trainee id: {}, count: {}", traineeId, trainings.size());
        return trainings;
    }

    public List<Training> findByTrainerId(Long trainerId) {
        List<Training> trainings = storage.values().stream()
                .map(obj -> (Training) obj)
                .filter(t -> trainerId.equals(t.getTrainerId()))
                .collect(Collectors.toList());
        logger.debug("Finding trainings by trainer id: {}, count: {}", trainerId, trainings.size());
        return trainings;
    }
}