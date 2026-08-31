package com.gym.trainerworkload.service;

import com.gym.trainerworkload.dto.request.WorkloadRequest;
import com.gym.trainerworkload.dto.response.MonthSummaryResponse;
import com.gym.trainerworkload.dto.response.TrainerWorkloadResponse;
import com.gym.trainerworkload.dto.response.YearSummaryResponse;
import com.gym.trainerworkload.model.ActionType;
import com.gym.trainerworkload.model.MonthSummary;
import com.gym.trainerworkload.model.TrainerSummary;
import com.gym.trainerworkload.model.YearSummary;
import com.gym.trainerworkload.repository.TrainerWorkloadRepository;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainerWorkloadService {

    private final TrainerWorkloadRepository repository;

    public void updateWorkload(WorkloadRequest request) {
        log.info("Starting workload calculation for trainer: {}", request.getTrainerUsername());
        TrainerSummary trainer = repository.getOrCreateTrainer(request.getTrainerUsername(), request);

        int year = request.getTrainingDate().getYear();
        int month = request.getTrainingDate().getMonthValue();
        int duration = request.getTrainingDuration();

        YearSummary yearSummary = trainer.getYears().computeIfAbsent(year,
                y -> YearSummary.builder()
                        .yearValue(y)
                        .months(new HashMap<>())
                        .build());

        MonthSummary monthSummary = yearSummary.getMonths().computeIfAbsent(month,
                m -> MonthSummary.builder()
                        .monthValue(m)
                        .trainingSummaryDuration(0)
                        .build());

        int currentDuration = monthSummary.getTrainingSummaryDuration();
        if (request.getActionType() == ActionType.ADD) {
            monthSummary.setTrainingSummaryDuration(currentDuration + duration);
        } else if (request.getActionType() == ActionType.DELETE) {
            monthSummary.setTrainingSummaryDuration(Math.max(0, currentDuration - duration));
        }
        log.info("Workload updated for trainer: {}", request.getTrainerUsername());
    }

    public TrainerWorkloadResponse getTrainerWorkload(String username) {
        log.info("Fetching workload for trainer: {}", username);
        TrainerSummary summary = repository.getTrainer(username)
                .orElseThrow(() -> new IllegalArgumentException("Trainer not found"));

        TrainerWorkloadResponse response = TrainerWorkloadResponse.builder()
                .trainerUsername(summary.getTrainerUsername())
                .trainerFirstName(summary.getTrainerFirstName())
                .trainerLastName(summary.getTrainerLastName())
                .trainerStatus(summary.isTrainerStatus())
                .years(List.of())
                .build();

        List<YearSummaryResponse> yearsList = summary.getYears().values().stream()
                .map(year -> new YearSummaryResponse(
                        year.getYearValue(),
                        year.getMonths().values().stream()
                                .map(month -> new MonthSummaryResponse(month.getMonthValue(), month.getTrainingSummaryDuration()))
                                .toList()
                ))
                .toList();

        response.setYears(yearsList);
        log.info("Workload fetched for trainer: {}", username);
        return response;
    }

}