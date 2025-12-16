package uz.wcaproject.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Training {
    private UUID id;
    private UUID traineeId;
    private UUID trainerId;
    private String trainingName;
    private UUID trainingType;
    private LocalDate trainingDate;
    private Integer trainingDuration;
}