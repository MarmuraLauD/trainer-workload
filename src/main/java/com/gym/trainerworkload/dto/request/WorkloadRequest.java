package com.gym.trainerworkload.dto.request;

import com.gym.trainerworkload.model.ActionType;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WorkloadRequest {

    private String trainerUsername;
    private String trainerFirstName;
    private String trainerLastName;
    private boolean isActive;
    private LocalDate trainingDate;
    private int trainingDuration;
    private ActionType actionType;

}
