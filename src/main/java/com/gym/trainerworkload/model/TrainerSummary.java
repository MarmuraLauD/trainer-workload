package com.gym.trainerworkload.model;

import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TrainerSummary {

    private String trainerUsername;
    private String trainerFirstName;
    private String trainerLastName;
    private boolean trainerStatus;

    private Map<Integer, YearSummary> years;

}