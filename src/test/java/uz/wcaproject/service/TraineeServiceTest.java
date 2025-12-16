package uz.wcaproject.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uz.wcaproject.dao.TraineeDAO;
import uz.wcaproject.generators.PasswordGenerator;
import uz.wcaproject.generators.UserNameGenerator;
import uz.wcaproject.model.Trainee;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

    @Mock
    private TraineeDAO traineeDAO;

    @Mock
    private UserNameGenerator userNameGenerator;

    @Mock
    private PasswordGenerator passwordGenerator;

    @InjectMocks
    private TraineeService traineeService;

    private Trainee trainee;

    @BeforeEach
    void setUp() {
        trainee = new Trainee(
                LocalDate.of(2000, 1, 1),
                "Tashkent"
        );
        trainee.setId(UUID.randomUUID());
        trainee.setFirstName("Ali");
        trainee.setLastName("Karimov");
    }

    // =========================
    // CREATE
    // =========================

    @Test
    void createTrainee_success() {
        when(userNameGenerator.generateUsername(any(), any(), any()))
                .thenReturn("akarimov");
        when(passwordGenerator.generatePassword())
                .thenReturn("securePass");
        when(traineeDAO.create(any(Trainee.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Trainee created = traineeService.createTrainee(trainee);

        assertNotNull(created);
        assertEquals("akarimov", created.getUsername());
        assertEquals("securePass", created.getPassword());
        assertTrue(created.getIsActive());

        verify(traineeDAO).create(created);
        verify(userNameGenerator).generateUsername(eq("Ali"), eq("Karimov"), any());
        verify(passwordGenerator).generatePassword();
    }

    // =========================
    // UPDATE
    // =========================

    @Test
    void updateTrainee_success() {
        trainee.setUsername("akarimov");

        Trainee existing = new Trainee(
                LocalDate.of(1999, 1, 1),
                "Old address"
        );
        existing.setId(trainee.getId());
        existing.setUsername("akarimov");

        when(traineeDAO.findByUsername("akarimov"))
                .thenReturn(Optional.of(existing));
        when(traineeDAO.update(any(Trainee.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Trainee updated = traineeService.updateTrainee(trainee);

        assertEquals("Ali", updated.getFirstName());
        assertEquals("Karimov", updated.getLastName());
        assertEquals("Tashkent", updated.getAddress());

        verify(traineeDAO).update(existing);
    }

    @Test
    void updateTrainee_notFound_throwsException() {
        trainee.setUsername("unknown");

        when(traineeDAO.findByUsername("unknown"))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> traineeService.updateTrainee(trainee));

        verify(traineeDAO, never()).update(any());
    }

    // =========================
    // DELETE
    // =========================

    @Test
    void deleteTrainee_success() {
        traineeService.deleteTrainee("akarimov");

        verify(traineeDAO).delete("akarimov");
    }

    // =========================
    // READ
    // =========================

    @Test
    void getTraineeById_success() {
        when(traineeDAO.findById("123"))
                .thenReturn(Optional.of(trainee));

        Optional<Trainee> result = traineeService.getTraineeById("123");

        assertTrue(result.isPresent());
        verify(traineeDAO).findById("123");
    }

    @Test
    void getTraineeByUsername_success() {
        when(traineeDAO.findByUsername("akarimov"))
                .thenReturn(Optional.of(trainee));

        Optional<Trainee> result =
                traineeService.getTraineeByUsername("akarimov");

        assertTrue(result.isPresent());
        verify(traineeDAO).findByUsername("akarimov");
    }

    @Test
    void getAllTrainees_success() {
        when(traineeDAO.findAll())
                .thenReturn(List.of(trainee));

        List<Trainee> result = traineeService.getAllTrainees();

        assertEquals(1, result.size());
        verify(traineeDAO).findAll();
    }
}
