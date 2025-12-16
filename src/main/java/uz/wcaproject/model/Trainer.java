package uz.wcaproject.model;

import lombok.*;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Trainer extends User {
    private UUID specialization;
}