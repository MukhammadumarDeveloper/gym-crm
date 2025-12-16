package uz.wcaproject.model;

import lombok.*;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Trainee extends User{
    private LocalDate dateOfBirth;
    private String address;
}
