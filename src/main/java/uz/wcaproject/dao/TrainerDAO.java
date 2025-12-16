package uz.wcaproject.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import uz.wcaproject.model.Trainer;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@Repository
public class TrainerDAO {
    private static final Logger logger = LoggerFactory.getLogger(TrainerDAO.class);

    private final Map<UUID, Trainer> storage;

    @Autowired
    public TrainerDAO(@Qualifier("trainerStorage") Map<UUID, Trainer> storage) {
        this.storage = storage;
    }

    public Trainer create(Trainer trainer) {
        UUID id = UUID.randomUUID();
        trainer.setId(id);
        storage.put(id, trainer);
        logger.info("Created trainer with id: {}", id);
        return trainer;
    }

    public Trainer update(Trainer trainer) {
        if (trainer.getId() == null || !storage.containsKey(trainer.getId())) {
            logger.error("Attempted to update non-existent trainer with id: {}", trainer.getId());
            throw new IllegalArgumentException("Trainer not found");
        }
        storage.put(trainer.getId(), trainer);
        logger.info("Updated trainer with id: {}", trainer.getId());
        return trainer;
    }

    public Optional<Trainer> findById(String id) {
        Trainer trainer = storage.get(UUID.fromString(id));
        logger.debug("Finding trainer by id: {}, found: {}", id, trainer != null);
        return Optional.ofNullable(trainer);
    }

    public List<Trainer> findAll() {
        List<Trainer> trainers = storage.values().stream()
                .toList();
        logger.debug("Finding all trainers, count: {}", trainers.size());
        return trainers;
    }

    public Optional<Trainer> findByUsername(String username) {
        Optional<Trainer> trainer = storage.values().stream()
                .filter(tr -> tr.getUsername().equals(username))
                .findFirst();
        logger.debug("Finding trainer by username: {}, found: {}", username, trainer.isPresent());
        return trainer;
    }
}