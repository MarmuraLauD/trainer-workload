package com.gym.trainerworkload.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class MonthSummary {

    private int monthValue;
    private int trainingSummaryDuration;

}