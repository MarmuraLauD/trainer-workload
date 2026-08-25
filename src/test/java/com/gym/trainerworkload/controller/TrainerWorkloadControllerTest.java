package com.gym.trainerworkload.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gym.trainerworkload.dto.request.WorkloadRequest;
import com.gym.trainerworkload.dto.response.TrainerWorkloadResponse;
import com.gym.trainerworkload.dto.response.YearSummaryResponse;
import com.gym.trainerworkload.service.TrainerWorkloadService;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TrainerWorkloadControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private TrainerWorkloadService trainerWorkloadService;

    @InjectMocks
    private TrainerWorkloadController trainerWorkloadController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(trainerWorkloadController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void postUpdate_callsService() throws Exception {
        // Arrange
        WorkloadRequest request = WorkloadRequest.builder()
                .trainerUsername("u")
                .trainerFirstName("F")
                .trainerLastName("L")
                .isActive(true)
                .trainingDate(LocalDate.now())
                .trainingDuration(30)
                .build();

        String json = objectMapper.writeValueAsString(request);

        // Act
        // Assert
        mockMvc.perform(post("/api/v1/workloads")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(trainerWorkloadService).updateWorkload(Mockito.any(WorkloadRequest.class));
    }

    @Test
    void getWorkload_returnsJson() throws Exception {
        // Arrange
        String username = "u1";
        TrainerWorkloadResponse response = TrainerWorkloadResponse.builder()
                .trainerUsername(username)
                .trainerFirstName("FN")
                .trainerLastName("LN")
                .trainerStatus(true)
                .years(List.of(new YearSummaryResponse(2025, List.of())))
                .build();

        when(trainerWorkloadService.getTrainerWorkload(username)).thenReturn(response);

        // Act
        // Assert
        mockMvc.perform(get("/api/v1/workloads/{username}", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trainerUsername").value("u1"))
                .andExpect(jsonPath("$.trainerFirstName").value("FN"));
    }

}
