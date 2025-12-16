package uz.wcaproject.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uz.wcaproject.dao.TrainingTypeDAO;
import uz.wcaproject.model.TrainingType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingTypeServiceTest {

    @Mock
    private TrainingTypeDAO trainingTypeDAO;

    @InjectMocks
    private TrainingTypeService trainingTypeService;

    private TrainingType trainingType;

    @BeforeEach
    void setUp() {
        trainingType = new TrainingType();
        trainingType.setId(UUID.randomUUID());
        trainingType.setTrainingTypeName("Cardio");
    }

    // =========================
    // GET BY NAME
    // =========================

    @Test
    void getTrainingType_success() {
        when(trainingTypeDAO.findByName("Cardio"))
                .thenReturn(Optional.of(trainingType));

        TrainingType result =
                trainingTypeService.getTrainingType("Cardio");

        assertNotNull(result);
        assertEquals("Cardio", result.getTrainingTypeName());

        verify(trainingTypeDAO).findByName("Cardio");
    }

    @Test
    void getTrainingType_notFound_throwsException() {
        when(trainingTypeDAO.findByName("Yoga"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> trainingTypeService.getTrainingType("Yoga")
        );

        assertEquals("Training type not found", exception.getMessage());
        verify(trainingTypeDAO).findByName("Yoga");
    }

    // =========================
    // GET ALL
    // =========================

    @Test
    void getAllTrainingTypes_success() {
        when(trainingTypeDAO.findAll())
                .thenReturn(List.of(trainingType));

        List<TrainingType> result =
                trainingTypeService.getAllTrainingTypes();

        assertEquals(1, result.size());
        assertEquals("Cardio", result.get(0).getTrainingTypeName());

        verify(trainingTypeDAO).findAll();
    }
}
