package uz.wcaproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.wcaproject.dao.TrainingTypeDAO;
import uz.wcaproject.model.TrainingType;

import java.util.List;

@Service
public class TrainingTypeService {

    private final TrainingTypeDAO trainingTypeDAO;

    @Autowired
    public TrainingTypeService(TrainingTypeDAO trainingTypeDAO) {
        this.trainingTypeDAO = trainingTypeDAO;
    }

    public TrainingType getTrainingType(String trainingTypeName) {
        return trainingTypeDAO.findByName(trainingTypeName).orElseThrow(() -> new IllegalArgumentException("Training type not found"));
    }

    public List<TrainingType> getAllTrainingTypes() {
        return trainingTypeDAO.findAll();
    }
}
