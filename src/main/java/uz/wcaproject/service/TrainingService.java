package uz.wcaproject.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.wcaproject.dao.TrainingDAO;
import uz.wcaproject.model.Training;

import java.util.List;
import java.util.Optional;
@Service
public class TrainingService {
    private static final Logger logger = LoggerFactory.getLogger(TrainingService.class);

    private TrainingDAO trainingDAO;

    @Autowired
    public void setTrainingDAO(TrainingDAO trainingDAO) {
        this.trainingDAO = trainingDAO;
    }

    public Training createTraining(Training training) {
        logger.info("Creating new training: {} for trainee id: {} and trainer id: {}",
                training.getTrainingName(), training.getTraineeId(), training.getTrainerId());

        Training created = trainingDAO.create(training);
        logger.info("Training created successfully with id: {}", created.getId());
        return created;
    }

    public Optional<Training> getTrainingById(Long id) {
        logger.debug("Retrieving training by id: {}", id);
        return trainingDAO.findById(id);
    }

    public List<Training> getAllTrainings() {
        logger.debug("Retrieving all trainings");
        return trainingDAO.findAll();
    }

    public List<Training> getTrainingsByTraineeId(Long traineeId) {
        logger.debug("Retrieving trainings for trainee id: {}", traineeId);
        return trainingDAO.findByTraineeId(traineeId);
    }

    public List<Training> getTrainingsByTrainerId(Long trainerId) {
        logger.debug("Retrieving trainings for trainer id: {}", trainerId);
        return trainingDAO.findByTrainerId(trainerId);
    }
}