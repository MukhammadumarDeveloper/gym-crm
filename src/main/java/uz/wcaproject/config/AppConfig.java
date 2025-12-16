package uz.wcaproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import uz.wcaproject.model.Trainee;
import uz.wcaproject.model.Trainer;
import uz.wcaproject.model.Training;
import uz.wcaproject.model.TrainingType;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Configuration
@ComponentScan(basePackages = "uz.wcaproject")
@PropertySource("classpath:application.properties")
public class AppConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean(name = "traineeStorage")
    public Map<UUID, Trainee> traineeStorage() {
        return new ConcurrentHashMap<>();
    }

    @Bean(name = "trainerStorage")
    public Map<UUID, Trainer> trainerStorage() {
        return new ConcurrentHashMap<>();
    }

    @Bean(name = "trainingStorage")
    public Map<UUID, Training> trainingStorage() {
        return new ConcurrentHashMap<>();
    }

    @Bean(name = "trainingTypeStorage")
    public Map<UUID, TrainingType> trainingTypeStorage() {
        return new ConcurrentHashMap<>();
    }

}