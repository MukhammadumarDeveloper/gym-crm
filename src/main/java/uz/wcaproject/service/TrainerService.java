package uz.wcaproject.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.wcaproject.model.Trainer;
import uz.wcaproject.model.User;

import java.util.List;
import java.util.Optional;
@Service
public class TrainerService {
    private static final Logger logger = LoggerFactory.getLogger(TrainerService.class);

    private TrainerDAO trainerDAO;
    private Storage storage;

    @Autowired
    public void setTrainerDAO(TrainerDAO trainerDAO) {
        this.trainerDAO = trainerDAO;
    }

    @Autowired
    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public Trainer createTrainer(Trainer trainer) {
        logger.info("Creating new trainer profile for: {} {}",
                trainer.getUser().getFirstName(), trainer.getUser().getLastName());

        User user = trainer.getUser();
        String username = storage.generateUsername(user.getFirstName(), user.getLastName());
        String password = storage.generatePassword();

        user.setUsername(username);
        user.setPassword(password);
        user.setIsActive(true);

        storage.getUserStorage().put(username, user);

        Trainer created = trainerDAO.create(trainer);
        logger.info("Trainer profile created successfully with username: {}", username);
        return created;
    }

    public Trainer updateTrainer(Trainer trainer) {
        logger.info("Updating trainer profile with id: {}", trainer.getId());

        Optional<Trainer> existing = trainerDAO.findById(trainer.getId());
        if (!existing.isPresent()) {
            logger.error("Trainer not found with id: {}", trainer.getId());
            throw new IllegalArgumentException("Trainer not found");
        }

        Trainer updated = trainerDAO.update(trainer);
        logger.info("Trainer profile updated successfully");
        return updated;
    }

    public Optional<Trainer> getTrainerById(Long id) {
        logger.debug("Retrieving trainer by id: {}", id);
        return trainerDAO.findById(id);
    }

    public Optional<Trainer> getTrainerByUsername(String username) {
        logger.debug("Retrieving trainer by username: {}", username);
        return trainerDAO.findByUsername(username);
    }

    public List<Trainer> getAllTrainers() {
        logger.debug("Retrieving all trainers");
        return trainerDAO.findAll();
    }
}