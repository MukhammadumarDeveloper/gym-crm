package uz.wcaproject.facade;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uz.wcaproject.model.Trainee;

import java.util.List;
import java.util.Optional;

@Component
public class GymFacade {
    private static final Logger logger = LoggerFactory.getLogger(GymFacade.class);

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    @Autowired
    public GymFacade(TraineeService traineeService,
                     TrainerService trainerService,
                     TrainingService trainingService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
        logger.info("GymFacade initialized with all services");
    }

    public Trainee createTrainee(Trainee trainee) {
        logger.info("Facade: Creating trainee");
        return traineeService.createTrainee(trainee);
    }

    public Trainee updateTrainee(Trainee trainee) {
        logger.info("Facade: Updating trainee with id: {}", trainee.getId());
        return traineeService.updateTrainee(trainee);
    }

    public void deleteTrainee(Long id) {
        logger.info("Facade: Deleting trainee with id: {}", id);
        traineeService.deleteTrainee(id);
    }

    public Optional<Trainee> getTraineeById(Long id) {
        return traineeService.getTraineeById(id);
    }

    public Optional<Trainee> getTraineeByUsername(String username) {
        return traineeService.getTraineeByUsername(username);
    }

    public List<Trainee> getAllTrainees() {
        return traineeService.getAllTrainees();
    }

    public Trainer createTrainer(Trainer trainer) {
        logger.info("Facade: Creating trainer");
        return trainerService.createTrainer(trainer);
    }

    public Trainer updateTrainer(Trainer trainer) {
        logger.info("Facade: Updating trainer with id: {}", trainer.getId());
        return trainerService.updateTrainer(trainer);
    }

    public Optional<Trainer> getTrainerById(Long id) {
        return trainerService.getTrainerById(id);
    }

    public Optional<Trainer> getTrainerByUsername(String username) {
        return trainerService.getTrainerByUsername(username);
    }

    public List<Trainer> getAllTrainers() {
        return trainerService.getAllTrainers();
    }

    public Training createTraining(Training training) {
        logger.info("Facade: Creating training");
        return trainingService.createTraining(training);
    }

    public Optional<Training> getTrainingById(Long id) {
        return trainingService.getTrainingById(id);
    }

    public List<Training> getAllTrainings() {
        return trainingService.getAllTrainings();
    }

    public List<Training> getTrainingsByTraineeId(Long traineeId) {
        return trainingService.getTrainingsByTraineeId(traineeId);
    }

    public List<Training> getTrainingsByTrainerId(Long trainerId) {
        return trainingService.getTrainingsByTrainerId(trainerId);
    }
}