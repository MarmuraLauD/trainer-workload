package com.gym.trainerworkload.service;

import com.gym.trainerworkload.dto.request.WorkloadRequest;
import com.gym.trainerworkload.model.ActionType;
import com.gym.trainerworkload.model.MonthSummary;
import com.gym.trainerworkload.model.TrainerSummary;
import com.gym.trainerworkload.model.YearSummary;
import com.gym.trainerworkload.repository.TrainerWorkloadRepository;
import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TrainerWorkloadServiceTest {

    @Mock
    private TrainerWorkloadRepository repository;

    @InjectMocks
    private TrainerWorkloadService service;

    @Test
    void updateWorkload_add_createsMonthAndAddsDuration() {
        // AAA
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
                .years(new ConcurrentHashMap<>())
                .build();

        when(repository.getOrCreateTrainer("u", request)).thenReturn(trainer);

        // AAA
        service.updateWorkload(request);

        // AAA
        YearSummary yearSummary = trainer.getYears().get(year);
        assertNotNull(yearSummary);
        MonthSummary monthSummary = yearSummary.getMonths().get(month);
        assertNotNull(monthSummary);
        assertEquals(duration, monthSummary.getTrainingSummaryDuration());
    }

    @Test
    void updateWorkload_delete_cannotGoBelowZero() {
        // AAA
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

        MonthSummary existingMonth = new MonthSummary(month, 30);
        YearSummary existingYear = YearSummary.builder().yearValue(year).months(new ConcurrentHashMap<>(Map.of(month, existingMonth))).build();
        TrainerSummary trainer = TrainerSummary.builder()
                .trainerUsername("u2")
                .trainerFirstName("F2")
                .trainerLastName("L2")
                .trainerStatus(true)
                .years(new ConcurrentHashMap<>(Map.of(year, existingYear)))
                .build();

        when(repository.getOrCreateTrainer("u2", request)).thenReturn(trainer);

        // AAA
        service.updateWorkload(request);

        // AAA
        MonthSummary monthSummary = trainer.getYears().get(year).getMonths().get(month);
        assertNotNull(monthSummary);
        assertEquals(0, monthSummary.getTrainingSummaryDuration());
    }

    @Test
    void getTrainerWorkload_returnsCorrectStructure() {
        // AAA
        int year = 2025;
        int month = 3;
        MonthSummary m = new MonthSummary(month, 45);
        YearSummary y = YearSummary.builder().yearValue(year).months(new ConcurrentHashMap<>(Map.of(month, m))).build();
        TrainerSummary trainer = TrainerSummary.builder()
                .trainerUsername("userX")
                .trainerFirstName("FN")
                .trainerLastName("LN")
                .trainerStatus(true)
                .years(new ConcurrentHashMap<>(Map.of(year, y)))
                .build();

        when(repository.getTrainer("userX")).thenReturn(java.util.Optional.of(trainer));

        // AAA
        var response = service.getTrainerWorkload("userX");

        // AAA
        assertEquals("userX", response.getTrainerUsername());
        assertEquals("FN", response.getTrainerFirstName());
        assertEquals("LN", response.getTrainerLastName());
        assertTrue(response.getYears()
                .stream()
                .anyMatch(yr -> yr.getYearValue() == year));
        var months = response.getYears()
                .stream()
                .filter(yr -> yr.getYearValue() == year)
                .findFirst().orElseThrow(() -> new AssertionError("Year not found"))
                .getMonths();
        assertTrue(months
                .stream()
                .anyMatch(ms -> ms.getMonthValue() == month && ms.getTrainingSummaryDuration() == 45));
    }
}
