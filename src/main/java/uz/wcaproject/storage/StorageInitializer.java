package uz.wcaproject.storage;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import uz.wcaproject.model.Trainee;
import uz.wcaproject.model.Trainer;
import uz.wcaproject.model.Training;
import uz.wcaproject.model.TrainingType;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Component
public class StorageInitializer {

    private static final Logger logger = LoggerFactory.getLogger(StorageInitializer.class);

    @Value("${storage.init.folder.path}")
    private String dataFolder;

    private final Map<UUID, Trainee> traineeStorage;
    private final Map<UUID, Trainer> trainerStorage;
    private final Map<UUID, Training> trainingStorage;
    private final Map<UUID, TrainingType> trainingTypeStorage;

    private final ObjectMapper mapper = new ObjectMapper();

    public StorageInitializer(
            Map<UUID, Trainee> traineeStorage,
            Map<UUID, Trainer> trainerStorage,
            Map<UUID, Training> trainingStorage,
            Map<UUID, TrainingType> trainingTypeStorage
    ) {
        this.traineeStorage = traineeStorage;
        this.trainerStorage = trainerStorage;
        this.trainingStorage = trainingStorage;
        this.trainingTypeStorage = trainingTypeStorage;
    }

    @PostConstruct
    public void init() {
        load("trainees.json", traineeStorage, new TypeReference<>() {});
        load("trainers.json", trainerStorage, new TypeReference<>() {});
        load("trainings.json", trainingStorage, new TypeReference<>() {});
        load("training-types.json", trainingTypeStorage, new TypeReference<>() {});
    }

    private <T> void load(String fileName, Map<UUID, T> target, TypeReference<Map<UUID, T>> typeRef) {
        try (FileInputStream fis = new FileInputStream(dataFolder + "/" + fileName)) {
            Map<UUID, T> data = mapper.readValue(fis, typeRef);
            target.putAll(data);
            logger.info("Loaded {} entries into {}", data.size(), fileName);
        } catch (JacksonException | IOException ex) {
            logger.error(ex.getMessage(), ex);
        } catch (Exception e) {
            logger.warn("Could not load {}", fileName, e);
        }
    }

    @PreDestroy
    public void destroy() {
        save("trainees.json", traineeStorage);
        save("trainers.json", trainerStorage);
        save("trainings.json", trainingStorage);
        save("training-types.json", trainingTypeStorage);
    }

    private <T> void save(String fileName, T target) {
        try (FileOutputStream fos = new FileOutputStream(dataFolder + "/" + fileName)) {
            mapper.writeValue(fos, target);
            logger.info("Saved {} into {}", target.getClass().getSimpleName(), fileName);
        } catch (IOException | JacksonException e) {
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            logger.warn("Could not save {}", fileName, e);
        }

    }
}