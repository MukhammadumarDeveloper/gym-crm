package uz.wcaproject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import uz.wcaproject.config.AppConfig;
import uz.wcaproject.facade.GymFacade;
import uz.wcaproject.model.Trainee;
import uz.wcaproject.model.Training;

import java.time.LocalDate;
import java.util.UUID;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);

        try {
            GymFacade facade = context.getBean(GymFacade.class);

            // =========================
            // READ DATA (from JSON)
            // =========================

            logger.info("=== All Training Types ===");
            facade.getAllTrainingTypes()
                    .forEach(tt -> logger.info(tt.toString()));

            logger.info("=== All Trainees ===");
            facade.getAllTrainees()
                    .forEach(t -> logger.info(t.toString()));

            logger.info("=== All Trainers ===");
            facade.getAllTrainers()
                    .forEach(t -> logger.info(t.toString()));

            logger.info("=== All Trainings ===");
            facade.getAllTrainings()
                    .forEach(t -> logger.info(t.toString()));

            // =========================
            // CREATE NEW TRAINEE
            // =========================

            Trainee newTrainee = new Trainee(
                    LocalDate.of(2002, 8, 15),
                    "Namangan, Uzbekistan"
            );

            newTrainee.setId(UUID.randomUUID());
            newTrainee.setFirstName("Aziz");
            newTrainee.setLastName("Usmonov");
            newTrainee.setUsername("ausmonov");
            newTrainee.setPassword("123456");
            newTrainee.setIsActive(true);

            facade.createTrainee(newTrainee);

            logger.info("Created trainee: {}", newTrainee.getUsername());

            // =========================
            // CREATE NEW TRAINING
            // =========================

            Training training = new Training(
                    UUID.randomUUID(),
                    newTrainee.getId(),
                    facade.getAllTrainers().get(0).getId(),
                    "Advanced Java",
                    facade.getAllTrainers().get(0).getSpecialization(),
                    LocalDate.now(),
                    120
            );

            facade.createTraining(training);
            logger.info("Created training: {}", training.getTrainingName());

        } finally {
            // 🔥 THIS IS CRITICAL
            context.close(); // triggers @PreDestroy
            logger.info("Spring context closed");
        }
    }
}
