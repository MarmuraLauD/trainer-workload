package com.gym.trainerworkload.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MonthSummary {

    private int monthValue;
    private int trainingSummaryDuration;

}