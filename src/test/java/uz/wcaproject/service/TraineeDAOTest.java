package uz.wcaproject.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uz.wcaproject.dao.TraineeDAO;
import uz.wcaproject.model.Trainee;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TraineeDAOTest {

    private TraineeDAO traineeDAO;
    private HashMap<UUID, Trainee> storage;

    @BeforeEach
    void setUp() {
        storage = new HashMap<>();
        traineeDAO = new TraineeDAO(storage);
    }

    @Test
    void create_shouldAssignIdAndStore() {
        Trainee trainee = new Trainee();
        trainee.setFirstName("John");
        trainee.setLastName("Doe");

        Trainee created = traineeDAO.create(trainee);

        assertNotNull(created.getId(), "ID should be assigned");
        assertEquals(trainee, storage.get(created.getId()), "Trainee should be stored in map");
    }

    @Test
    void update_existingTrainee_shouldUpdateFields() {
        Trainee trainee = new Trainee();
        trainee.setFirstName("John");
        trainee.setLastName("Doe");
        traineeDAO.create(trainee);

        trainee.setFirstName("Jane");
        Trainee updated = traineeDAO.update(trainee);

        assertEquals("Jane", storage.get(updated.getId()).getFirstName(), "First name should be updated");
    }

    @Test
    void update_nonExistingTrainee_shouldThrow() {
        Trainee trainee = new Trainee();
        trainee.setId(UUID.randomUUID());

        assertThrows(IllegalArgumentException.class, () -> traineeDAO.update(trainee));
    }

    @Test
    void delete_existingTrainee_shouldRemoveFromStorage() {
        Trainee trainee = new Trainee();
        trainee.setFirstName("John");
        trainee.setLastName("Doe");
        trainee.setUsername("John.Doe");
        traineeDAO.create(trainee);

        traineeDAO.delete("John.Doe");

        assertTrue(storage.isEmpty(), "Storage should be empty after deletion");
    }

    @Test
    void findById_existingTrainee_shouldReturnOptional() {
        Trainee trainee = new Trainee();
        traineeDAO.create(trainee);

        Optional<Trainee> result = traineeDAO.findById(trainee.getId().toString());

        assertTrue(result.isPresent(), "Trainee should be found by ID");
        assertEquals(trainee, result.get());
    }

    @Test
    void findById_nonExisting_shouldReturnEmpty() {
        Optional<Trainee> result = traineeDAO.findById(UUID.randomUUID().toString());
        assertTrue(result.isEmpty(), "Should return empty for unknown ID");
    }

    @Test
    void findByUsername_existing_shouldReturnOptional() {
        Trainee trainee = new Trainee();
        trainee.setUsername("john.doe");
        traineeDAO.create(trainee);

        Optional<Trainee> result = traineeDAO.findByUsername("john.doe");
        assertTrue(result.isPresent());
        assertEquals(trainee, result.get());
    }

    @Test
    void findByUsername_nonExisting_shouldReturnEmpty() {
        Optional<Trainee> result = traineeDAO.findByUsername("unknown");
        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_shouldReturnAllTrainees() {
        Trainee t1 = new Trainee();
        Trainee t2 = new Trainee();
        traineeDAO.create(t1);
        traineeDAO.create(t2);

        List<Trainee> all = traineeDAO.findAll();

        assertEquals(2, all.size());
        assertTrue(all.contains(t1));
        assertTrue(all.contains(t2));
    }
}
