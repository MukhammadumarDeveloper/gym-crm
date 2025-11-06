package uz.wcaproject.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import uz.wcaproject.storage.Storage;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
@Configuration
@ComponentScan(basePackages = "uz.wcaproject")
@PropertySource("classpath:application.properties")
public class AppConfig {

    @Value("${storage.init.file.path}")
    private String initFilePath;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean(name = "traineeStorage")
    public Map<Long, Object> traineeStorage() {
        return new ConcurrentHashMap<>();
    }

    @Bean(name = "trainerStorage")
    public Map<Long, Object> trainerStorage() {
        return new ConcurrentHashMap<>();
    }

    @Bean(name = "trainingStorage")
    public Map<Long, Object> trainingStorage() {
        return new ConcurrentHashMap<>();
    }

    @Bean(name = "trainingTypeStorage")
    public Map<Long, Object> trainingTypeStorage() {
        return new ConcurrentHashMap<>();
    }

    @Bean(name = "userStorage")
    public Map<String, Object> userStorage() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public Storage storage() {
        return new Storage(initFilePath);
    }
}