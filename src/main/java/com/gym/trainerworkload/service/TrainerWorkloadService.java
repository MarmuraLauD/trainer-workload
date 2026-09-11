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
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainerWorkloadService {

    private final TrainerWorkloadRepository repository;

    public void updateWorkload(WorkloadRequest request) {
        String transactionId = MDC.get("transactionId");
        log.info("[Transaction: {}] Starting workload calculation for trainer: {}", transactionId, request.getTrainerUsername());

        TrainerSummary trainer = repository.findByTrainerUsername(request.getTrainerUsername())
                .orElseGet(() -> TrainerSummary.builder()
                        .trainerUsername(request.getTrainerUsername())
                        .trainerFirstName(request.getTrainerFirstName())
                        .trainerLastName(request.getTrainerLastName())
                        .trainerStatus(request.getIsActive())
                        .years(new ArrayList<>())
                        .build());

        int year = request.getTrainingDate().getYear();
        int month = request.getTrainingDate().getMonthValue();
        int duration = request.getTrainingDuration();

        YearSummary yearSummary = trainer.getYears().stream()
                .filter(y -> y.getYearValue() == year)
                .findFirst()
                .orElseGet(() -> {
                    YearSummary newYear = YearSummary.builder()
                            .yearValue(year)
                            .months(new ArrayList<>())
                            .build();
                    trainer.getYears().add(newYear);
                    return newYear;
                });

        MonthSummary monthSummary = yearSummary.getMonths().stream()
                .filter(m -> m.getMonthValue() == month)
                .findFirst()
                .orElseGet(() -> {
                    MonthSummary newMonth = MonthSummary.builder()
                            .monthValue(month)
                            .trainingSummaryDuration(0)
                            .build();
                    yearSummary.getMonths().add(newMonth);
                    return newMonth;
                });

        int currentDuration = monthSummary.getTrainingSummaryDuration();
        if (request.getActionType() == ActionType.ADD) {
            monthSummary.setTrainingSummaryDuration(currentDuration + duration);
            log.debug("[Transaction: {}] Added {} to duration. New total: {}", transactionId, duration, monthSummary.getTrainingSummaryDuration());
        } else if (request.getActionType() == ActionType.DELETE) {
            monthSummary.setTrainingSummaryDuration(Math.max(0, currentDuration - duration));
            log.debug("[Transaction: {}] Subtracted {} from duration. New total: {}", transactionId, duration, monthSummary.getTrainingSummaryDuration());
        }

        repository.save(trainer);
        log.info("[Transaction: {}] Workload updated successfully for trainer: {}", transactionId, request.getTrainerUsername());
    }

    public TrainerWorkloadResponse getTrainerWorkload(String username) {
        String transactionId = MDC.get("transactionId");
        log.info("[Transaction: {}] Fetching workload for trainer: {}", transactionId, username);

        TrainerSummary summary = repository.findByTrainerUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Trainer not found"));

        TrainerWorkloadResponse response = TrainerWorkloadResponse.builder()
                .trainerUsername(summary.getTrainerUsername())
                .trainerFirstName(summary.getTrainerFirstName())
                .trainerLastName(summary.getTrainerLastName())
                .trainerStatus(summary.isTrainerStatus())
                .years(new ArrayList<>())
                .build();

        List<YearSummaryResponse> yearsList = summary.getYears().stream()
                .map(year -> new YearSummaryResponse(
                        year.getYearValue(),
                        year.getMonths().stream()
                                .map(m -> new MonthSummaryResponse(m.getMonthValue(), m.getTrainingSummaryDuration()))
                                .toList()
                ))
                .toList();

        response.setYears(yearsList);
        log.info("[Transaction: {}] Workload fetched successfully for trainer: {}", transactionId, username);
        return response;
    }
}