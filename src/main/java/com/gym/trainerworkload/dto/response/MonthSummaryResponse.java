package com.gym.trainerworkload.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class MonthSummaryResponse {

    private int monthValue;
    private int trainingSummaryDuration;

}