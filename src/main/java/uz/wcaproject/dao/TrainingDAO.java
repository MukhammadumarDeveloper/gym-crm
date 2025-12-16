package uz.wcaproject.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import uz.wcaproject.model.Training;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class TrainingDAO {
    private static final Logger logger = LoggerFactory.getLogger(TrainingDAO.class);

    private  final Map<UUID, Training> storage;

    @Autowired
    public TrainingDAO(@Qualifier("trainingStorage") Map<UUID, Training> storage) {
        this.storage = storage;
    }

    public Training create(Training training) {
        UUID id = UUID.randomUUID();
        training.setId(id);
        storage.put(id, training);
        logger.info("Created training with id: {}", id);
        return training;
    }

    public Optional<Training> findById(String id) {
        Training training = storage.get(UUID.fromString(id));
        logger.debug("Finding training by id: {}, found: {}", id, training != null);
        return Optional.ofNullable(training);
    }

    public List<Training> findAll() {
        List<Training> trainings = storage.values().stream()
                .toList();
        logger.debug("Finding all trainings, count: {}", trainings.size());
        return trainings;
    }

    public List<Training> findByTraineeId(String traineeId) {
        List<Training> trainings = storage.values().stream()
                .filter(t -> false)
                .collect(Collectors.toList());
        logger.debug("Finding trainings by trainee id: {}, count: {}", traineeId, trainings.size());
        return trainings;
    }

    public List<Training> findByTrainerId(String trainerId) {
        List<Training> trainings = storage.values().stream()
                .filter(t -> false)
                .collect(Collectors.toList());
        logger.debug("Finding trainings by trainer id: {}, count: {}", trainerId, trainings.size());
        return trainings;
    }
}