package com.gym.trainerworkload.service;

import com.gym.trainerworkload.dto.request.WorkloadRequest;
import com.gym.trainerworkload.model.ActionType;
import com.gym.trainerworkload.model.MonthSummary;
import com.gym.trainerworkload.model.TrainerSummary;
import com.gym.trainerworkload.model.YearSummary;
import com.gym.trainerworkload.repository.TrainerWorkloadRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerWorkloadServiceTest {

    @Mock
    private TrainerWorkloadRepository repository;

    @InjectMocks
    private TrainerWorkloadService service;

    @Test
    void updateWorkload_add_createsMonthAndAddsDuration() {
        int year = 2026;
        int month = 5;
        int duration = 60;

        WorkloadRequest request = WorkloadRequest.builder()
                .trainerUsername("u")
                .trainerFirstName("F")
                .trainerLastName("L")
                .trainingDate(LocalDate.of(year, month, 1))
                .trainingDuration(duration)
                .actionType(ActionType.ADD)
                .isActive(true)
                .build();

        TrainerSummary trainer = TrainerSummary.builder()
                .trainerUsername("u")
                .trainerFirstName("F")
                .trainerLastName("L")
                .trainerStatus(true)
                .years(new ArrayList<>())
                .build();

        when(repository.findByTrainerUsername("u")).thenReturn(Optional.of(trainer));

        service.updateWorkload(request);

        verify(repository).save(trainer);
        assertEquals(1, trainer.getYears().size());

        YearSummary yearSummary = trainer.getYears().getFirst();
        assertEquals(year, yearSummary.getYearValue());
        assertEquals(1, yearSummary.getMonths().size());

        MonthSummary monthSummary = yearSummary.getMonths().getFirst();
        assertEquals(month, monthSummary.getMonthValue());
        assertEquals(duration, monthSummary.getTrainingSummaryDuration());
    }

    @Test
    void updateWorkload_delete_cannotGoBelowZero() {
        int year = 2026;
        int month = 6;

        WorkloadRequest request = WorkloadRequest.builder()
                .trainerUsername("u2")
                .trainerFirstName("F2")
                .trainerLastName("L2")
                .trainingDate(LocalDate.of(year, month, 1))
                .trainingDuration(100)
                .actionType(ActionType.DELETE)
                .isActive(true)
                .build();

        MonthSummary existingMonth = MonthSummary.builder()
                .monthValue(month)
                .trainingSummaryDuration(30)
                .build();

        YearSummary existingYear = YearSummary.builder()
                .yearValue(year)
                .months(new ArrayList<>(List.of(existingMonth)))
                .build();

        TrainerSummary trainer = TrainerSummary.builder()
                .trainerUsername("u2")
                .trainerFirstName("F2")
                .trainerLastName("L2")
                .trainerStatus(true)
                .years(new ArrayList<>(List.of(existingYear)))
                .build();

        when(repository.findByTrainerUsername("u2")).thenReturn(Optional.of(trainer));

        service.updateWorkload(request);

        verify(repository).save(trainer);
        MonthSummary monthSummary = trainer.getYears().getFirst().getMonths().getFirst();
        assertEquals(0, monthSummary.getTrainingSummaryDuration());
    }

    @Test
    void getTrainerWorkload_returnsCorrectStructure() {
        int year = 2025;
        int month = 3;

        MonthSummary m = MonthSummary.builder()
                .monthValue(month)
                .trainingSummaryDuration(45)
                .build();

        YearSummary y = YearSummary.builder()
                .yearValue(year)
                .months(new ArrayList<>(List.of(m)))
                .build();

        TrainerSummary trainer = TrainerSummary.builder()
                .trainerUsername("userX")
                .trainerFirstName("FN")
                .trainerLastName("LN")
                .trainerStatus(true)
                .years(new ArrayList<>(List.of(y)))
                .build();

        when(repository.findByTrainerUsername("userX")).thenReturn(Optional.of(trainer));

        var response = service.getTrainerWorkload("userX");

        assertEquals("userX", response.getTrainerUsername());
        assertEquals("FN", response.getTrainerFirstName());
        assertEquals("LN", response.getTrainerLastName());

        assertTrue(response.getYears().stream().anyMatch(yr -> yr.getYearValue() == year));

        var months = response.getYears().stream()
                .filter(yr -> yr.getYearValue() == year)
                .findFirst()
                .orElseThrow()
                .getMonths();

        assertTrue(months.stream().anyMatch(ms -> ms.getMonthValue() == month && ms.getTrainingSummaryDuration() == 45));
    }
}