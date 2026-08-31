package com.gym.trainerworkload.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class YearSummaryResponse {

    private int yearValue;
    private List<MonthSummaryResponse> months;

}
