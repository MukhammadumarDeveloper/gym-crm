package uz.wcaproject.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.wcaproject.dao.TrainerDAO;
import uz.wcaproject.generators.PasswordGenerator;
import uz.wcaproject.generators.UserNameGenerator;
import uz.wcaproject.model.Trainer;

import java.util.List;
import java.util.Optional;

@Service
public class TrainerService {

    private static final Logger logger = LoggerFactory.getLogger(TrainerService.class);

    private final TrainerDAO trainerDAO;
    private final PasswordGenerator passwordGenerator;
    private final UserNameGenerator userNameGenerator;


    @Autowired
    public TrainerService(TrainerDAO trainerDAO, PasswordGenerator passwordGenerator, UserNameGenerator userNameGenerator) {
        this.trainerDAO = trainerDAO;
        this.passwordGenerator = passwordGenerator;
        this.userNameGenerator = userNameGenerator;
    }

    public Trainer createTrainer(Trainer trainer) {
        logger.info("Creating new trainer profile for: {} {}",
                trainer.getFirstName(), trainer.getLastName());

        String username = userNameGenerator.generateUsername(
                trainer.getFirstName(),
                trainer.getLastName(),
                name -> trainerDAO.findByUsername(name).isPresent()
        );

        String password = passwordGenerator.generatePassword();

        trainer.setUsername(username);
        trainer.setPassword(password);
        trainer.setIsActive(true);

        Trainer created = trainerDAO.create(trainer);
        logger.info("Trainer profile created successfully with username: {}", username);
        return created;
    }

    public Trainer updateTrainer(Trainer trainer) {
        logger.info("Updating trainer profile with username: {}", trainer.getUsername());

        Trainer existing = trainerDAO.findByUsername(trainer.getUsername())
                .orElseThrow(() -> {
                    logger.error("Trainer not found with username: {}", trainer.getUsername());
                    return new IllegalArgumentException("Trainer not found");
                });

        existing.setFirstName(trainer.getFirstName());
        existing.setLastName(trainer.getLastName());
        existing.setSpecialization(trainer.getSpecialization());
        existing.setIsActive(trainer.getIsActive());

        Trainer updated = trainerDAO.update(existing);
        logger.info("Trainer profile updated successfully");
        return updated;
    }

    public Optional<Trainer> getTrainerById(String id) {
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
