package uz.wcaproject.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uz.wcaproject.dao.TrainingDAO;
import uz.wcaproject.model.Training;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    @Mock
    private TrainingDAO trainingDAO;

    @InjectMocks
    private TrainingService trainingService;

    private Training training;

    @BeforeEach
    void setUp() {
        training = new Training();
        training.setId(UUID.randomUUID());
        training.setTraineeId(UUID.randomUUID());
        training.setTrainerId(UUID.randomUUID());
        training.setTrainingName("Strength Training");
        training.setTrainingType(UUID.randomUUID());
        training.setTrainingDate(LocalDate.now());
        training.setTrainingDuration(60);
    }

    // =========================
    // CREATE
    // =========================

    @Test
    void createTraining_success() {
        when(trainingDAO.create(any(Training.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Training created = trainingService.createTraining(training);

        assertNotNull(created);
        assertEquals("Strength Training", created.getTrainingName());
        assertEquals(training.getTraineeId(), created.getTraineeId());
        assertEquals(training.getTrainerId(), created.getTrainerId());

        verify(trainingDAO).create(training);
    }

    // =========================
    // READ
    // =========================

    @Test
    void getTrainingById_success() {
        when(trainingDAO.findById(training.getId().toString()))
                .thenReturn(Optional.of(training));

        Optional<Training> result =
                trainingService.getTrainingById(training.getId().toString());

        assertTrue(result.isPresent());
        assertEquals(training, result.get());

        verify(trainingDAO).findById(training.getId().toString());
    }

    @Test
    void getAllTrainings_success() {
        when(trainingDAO.findAll())
                .thenReturn(List.of(training));

        List<Training> result = trainingService.getAllTrainings();

        assertEquals(1, result.size());
        verify(trainingDAO).findAll();
    }

    @Test
    void getTrainingsByTraineeId_success() {
        when(trainingDAO.findByTraineeId(training.getTraineeId().toString()))
                .thenReturn(List.of(training));

        List<Training> result =
                trainingService.getTrainingsByTraineeId(training.getTraineeId().toString());

        assertEquals(1, result.size());
        assertEquals(training.getTraineeId(), result.get(0).getTraineeId());

        verify(trainingDAO).findByTraineeId(training.getTraineeId().toString());
    }

    @Test
    void getTrainingsByTrainerId_success() {
        when(trainingDAO.findByTrainerId(training.getTrainerId().toString()))
                .thenReturn(List.of(training));

        List<Training> result =
                trainingService.getTrainingsByTrainerId(training.getTrainerId().toString());

        assertEquals(1, result.size());
        assertEquals(training.getTrainerId(), result.get(0).getTrainerId());

        verify(trainingDAO).findByTrainerId(training.getTrainerId().toString());
    }
}
