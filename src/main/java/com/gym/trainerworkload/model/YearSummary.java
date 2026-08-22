package com.gym.trainerworkload.model;

import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class YearSummary {

    private int yearValue;
    private Map<Integer, MonthSummary> months;

}