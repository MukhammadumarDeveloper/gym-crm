package uz.wcaproject.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uz.wcaproject.dao.TrainerDAO;
import uz.wcaproject.generators.PasswordGenerator;
import uz.wcaproject.generators.UserNameGenerator;
import uz.wcaproject.model.Trainer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @Mock
    private TrainerDAO trainerDAO;

    @Mock
    private PasswordGenerator passwordGenerator;

    @Mock
    private UserNameGenerator userNameGenerator;

    @InjectMocks
    private TrainerService trainerService;

    private Trainer trainer;

    @BeforeEach
    void setUp() {
        trainer = new Trainer();
        trainer.setId(UUID.randomUUID());
        trainer.setFirstName("John");
        trainer.setLastName("Smith");
        trainer.setSpecialization(UUID.randomUUID());
        trainer.setIsActive(true);
    }

    // =========================
    // CREATE
    // =========================

    @Test
    void createTrainer_success() {
        when(userNameGenerator.generateUsername(any(), any(), any()))
                .thenReturn("john.smith");
        when(passwordGenerator.generatePassword())
                .thenReturn("securePass");
        when(trainerDAO.create(any(Trainer.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Trainer created = trainerService.createTrainer(trainer);

        assertNotNull(created);
        assertEquals("john.smith", created.getUsername());
        assertEquals("securePass", created.getPassword());
        assertTrue(created.getIsActive());

        verify(userNameGenerator)
                .generateUsername(eq("John"), eq("Smith"), any());
        verify(passwordGenerator).generatePassword();
        verify(trainerDAO).create(created);
    }

    // =========================
    // UPDATE
    // =========================

    @Test
    void updateTrainer_success() {
        trainer.setUsername("john.smith");

        Trainer existing = new Trainer();
        existing.setId(trainer.getId());
        existing.setUsername("john.smith");

        when(trainerDAO.findByUsername("john.smith"))
                .thenReturn(Optional.of(existing));
        when(trainerDAO.update(any(Trainer.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Trainer updated = trainerService.updateTrainer(trainer);

        assertEquals("John", updated.getFirstName());
        assertEquals("Smith", updated.getLastName());
        assertEquals(trainer.getSpecialization(), updated.getSpecialization());
        assertTrue(updated.getIsActive());

        verify(trainerDAO).update(existing);
    }

    @Test
    void updateTrainer_notFound_throwsException() {
        trainer.setUsername("unknown");

        when(trainerDAO.findByUsername("unknown"))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> trainerService.updateTrainer(trainer));

        verify(trainerDAO, never()).update(any());
    }

    // =========================
    // READ
    // =========================

    @Test
    void getTrainerById_success() {
        when(trainerDAO.findById("123"))
                .thenReturn(Optional.of(trainer));

        Optional<Trainer> result = trainerService.getTrainerById("123");

        assertTrue(result.isPresent());
        verify(trainerDAO).findById("123");
    }

    @Test
    void getTrainerByUsername_success() {
        when(trainerDAO.findByUsername("john.smith"))
                .thenReturn(Optional.of(trainer));

        Optional<Trainer> result =
                trainerService.getTrainerByUsername("john.smith");

        assertTrue(result.isPresent());
        verify(trainerDAO).findByUsername("john.smith");
    }

    @Test
    void getAllTrainers_success() {
        when(trainerDAO.findAll())
                .thenReturn(List.of(trainer));

        List<Trainer> result = trainerService.getAllTrainers();

        assertEquals(1, result.size());
        verify(trainerDAO).findAll();
    }
}
