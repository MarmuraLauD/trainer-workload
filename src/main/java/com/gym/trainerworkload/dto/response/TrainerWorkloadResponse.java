package com.gym.trainerworkload.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TrainerWorkloadResponse {

    private String trainerUsername;
    private String trainerFirstName;
    private String trainerLastName;
    private boolean trainerStatus;
    private List<YearSummaryResponse> years;

}
