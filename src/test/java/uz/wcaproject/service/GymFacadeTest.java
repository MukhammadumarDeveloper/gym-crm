package uz.wcaproject.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uz.wcaproject.facade.GymFacade;
import uz.wcaproject.model.*;
import uz.wcaproject.service.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GymFacadeTest {

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TrainingService trainingService;

    @Mock
    private TrainingTypeService trainingTypeService;

    @InjectMocks
    private GymFacade gymFacade;

    private Trainee trainee;
    private Trainer trainer;
    private Training training;
    private TrainingType trainingType;

    @BeforeEach
    void setUp() {
        trainee = new Trainee(
                LocalDate.of(2000, 1, 1),
                "Tashkent"
        );
        trainee.setId(UUID.randomUUID());
        trainee.setUsername("john.doe");

        trainer = new Trainer();
        trainer.setId(UUID.randomUUID());
        trainer.setUsername("trainer.one");

        trainingType = new TrainingType();
        trainingType.setId(UUID.randomUUID());
        trainingType.setTrainingTypeName("Java");

        training = new Training(
                UUID.randomUUID(),
                trainee.getId(),
                trainer.getId(),
                "Spring Basics",
                trainingType.getId(),
                LocalDate.now(),
                90
        );
    }

    // =========================
    // TRAINEE
    // =========================

    @Test
    void createTrainee_success() {
        when(traineeService.createTrainee(trainee)).thenReturn(trainee);

        Trainee result = gymFacade.createTrainee(trainee);

        assertNotNull(result);
        verify(traineeService).createTrainee(trainee);
    }

    @Test
    void updateTrainee_success() {
        when(traineeService.updateTrainee(trainee)).thenReturn(trainee);

        Trainee result = gymFacade.updateTrainee(trainee);

        assertEquals(trainee, result);
        verify(traineeService).updateTrainee(trainee);
    }

    @Test
    void deleteTrainee_success() {
        gymFacade.deleteTrainee("john.doe");

        verify(traineeService).deleteTrainee("john.doe");
    }

    @Test
    void getAllTrainees_success() {
        when(traineeService.getAllTrainees()).thenReturn(List.of(trainee));

        List<Trainee> result = gymFacade.getAllTrainees();

        assertEquals(1, result.size());
        verify(traineeService).getAllTrainees();
    }

    // =========================
    // TRAINER
    // =========================

    @Test
    void createTrainer_success() {
        when(trainerService.createTrainer(trainer)).thenReturn(trainer);

        Trainer result = gymFacade.createTrainer(trainer);

        assertNotNull(result);
        verify(trainerService).createTrainer(trainer);
    }

    @Test
    void updateTrainer_setsSpecializationAndUpdates() {
        when(trainingTypeService.getTrainingType("Java")).thenReturn(trainingType);
        when(trainerService.updateTrainer(trainer)).thenReturn(trainer);

        Trainer result = gymFacade.updateTrainer(trainer, "Java");

        assertEquals(trainingType.getId(), trainer.getSpecialization());
        verify(trainingTypeService).getTrainingType("Java");
        verify(trainerService).updateTrainer(trainer);
    }

    @Test
    void getAllTrainers_success() {
        when(trainerService.getAllTrainers()).thenReturn(List.of(trainer));

        List<Trainer> result = gymFacade.getAllTrainers();

        assertEquals(1, result.size());
        verify(trainerService).getAllTrainers();
    }

    // =========================
    // TRAINING TYPE
    // =========================

    @Test
    void getAllTrainingTypes_success() {
        when(trainingTypeService.getAllTrainingTypes()).thenReturn(List.of(trainingType));

        List<TrainingType> result = gymFacade.getAllTrainingTypes();

        assertEquals(1, result.size());
        verify(trainingTypeService).getAllTrainingTypes();
    }

    // =========================
    // TRAINING
    // =========================

    @Test
    void createTraining_success() {
        when(trainingService.createTraining(training)).thenReturn(training);

        Training result = gymFacade.createTraining(training);

        assertNotNull(result);
        verify(trainingService).createTraining(training);
    }

    @Test
    void getAllTrainings_success() {
        when(trainingService.getAllTrainings()).thenReturn(List.of(training));

        List<Training> result = gymFacade.getAllTrainings();

        assertEquals(1, result.size());
        verify(trainingService).getAllTrainings();
    }

    // =========================
// TRAINEE – QUERY METHODS
// =========================

    @Test
    void getTraineeById_success() {
        when(traineeService.getTraineeById("123")).thenReturn(Optional.of(trainee));

        Optional<Trainee> result = gymFacade.getTraineeById("123");

        assertTrue(result.isPresent());
        verify(traineeService).getTraineeById("123");
    }

    @Test
    void getTraineeByUsername_success() {
        when(traineeService.getTraineeByUsername("john.doe"))
                .thenReturn(Optional.of(trainee));

        Optional<Trainee> result = gymFacade.getTraineeByUsername("john.doe");

        assertTrue(result.isPresent());
        verify(traineeService).getTraineeByUsername("john.doe");
    }

// =========================
// TRAINER – QUERY METHODS
// =========================

    @Test
    void getTrainerById_success() {
        when(trainerService.getTrainerById("321")).thenReturn(Optional.of(trainer));

        Optional<Trainer> result = gymFacade.getTrainerById("321");

        assertTrue(result.isPresent());
        verify(trainerService).getTrainerById("321");
    }

    @Test
    void getTrainerByUsername_success() {
        when(trainerService.getTrainerByUsername("trainer.one"))
                .thenReturn(Optional.of(trainer));

        Optional<Trainer> result = gymFacade.getTrainerByUsername("trainer.one");

        assertTrue(result.isPresent());
        verify(trainerService).getTrainerByUsername("trainer.one");
    }

// =========================
// TRAINING – QUERY METHODS
// =========================

    @Test
    void getTrainingById_success() {
        when(trainingService.getTrainingById("555"))
                .thenReturn(Optional.of(training));

        Optional<Training> result = gymFacade.getTrainingById("555");

        assertTrue(result.isPresent());
        verify(trainingService).getTrainingById("555");
    }

    @Test
    void getTrainingsByTraineeId_success() {
        Long traineeId = 10L;

        when(trainingService.getTrainingsByTraineeId("10"))
                .thenReturn(List.of(training));

        List<Training> result = gymFacade.getTrainingsByTraineeId(traineeId);

        assertEquals(1, result.size());
        verify(trainingService).getTrainingsByTraineeId("10");
    }

    @Test
    void getTrainingsByTrainerId_success() {
        Long trainerId = 20L;

        when(trainingService.getTrainingsByTrainerId("20"))
                .thenReturn(List.of(training));

        List<Training> result = gymFacade.getTrainingsByTrainerId(trainerId);

        assertEquals(1, result.size());
        verify(trainingService).getTrainingsByTrainerId("20");
    }

}
