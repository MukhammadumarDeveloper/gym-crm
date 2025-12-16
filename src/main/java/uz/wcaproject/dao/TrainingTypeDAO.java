package uz.wcaproject.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import uz.wcaproject.model.TrainingType;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class TrainingTypeDAO {

    private static final Logger logger = LoggerFactory.getLogger(TrainingTypeDAO.class);

    private final Map<UUID, TrainingType> storage;

    @Autowired
    public TrainingTypeDAO(@Qualifier("trainingTypeStorage") Map<UUID, TrainingType> storage) {
        this.storage = storage;
    }

    public TrainingType create(TrainingType trainingType) {
        UUID id = UUID.randomUUID();
        trainingType.setId(id);
        storage.put(id, trainingType);

        logger.info("Created trainingType with id: {}", id);
        return trainingType;
    }

    public TrainingType update(TrainingType trainingType) {
        if (trainingType.getId() == null || !storage.containsKey(trainingType.getId())) {
            logger.error("Attempted to update non-existent TrainingType with id: {}", trainingType.getId());
            throw new IllegalArgumentException("TrainingType not found");
        }

        storage.put(trainingType.getId(), trainingType);
        logger.info("Updated TrainingType with id: {}", trainingType.getId());
        return trainingType;
    }

    public void delete(UUID id) {
        if (storage.remove(id) != null) {
            logger.info("Deleted TrainingType with id: {}", id);
        } else {
            logger.warn("Attempted to delete non-existent TrainingType with id: {}", id);
        }
    }

    public Optional<TrainingType> findById(UUID id) {
        TrainingType trainingType = storage.get(id);

        logger.debug("Finding TrainingType by id: {}, found: {}", id, trainingType != null);
        return Optional.ofNullable(trainingType);
    }

    public List<TrainingType> findAll() {
        List<TrainingType> list = storage.values()
                .stream()
                .collect(Collectors.toList());

        logger.debug("Finding all TrainingTypes, count: {}", list.size());
        return list;
    }

    public Optional<TrainingType> findByName(String trainingTypeName) {
        Optional<TrainingType> result = storage.values()
                .stream()
                .filter(t -> t.getTrainingTypeName().equalsIgnoreCase(trainingTypeName))
                .findFirst();

        logger.debug("Finding TrainingType by name: {}, found: {}", trainingTypeName, result.isPresent());
        return result;
    }
}