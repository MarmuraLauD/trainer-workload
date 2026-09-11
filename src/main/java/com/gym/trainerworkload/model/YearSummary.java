package com.gym.trainerworkload.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class YearSummary {

    private int yearValue;

    @Builder.Default
    private List<MonthSummary> months = new ArrayList<>();

}