package com.gym.trainerworkload.controller;

import com.gym.trainerworkload.controller.api.TrainerWorkloadControllerAPI;
import com.gym.trainerworkload.dto.request.WorkloadRequest;
import com.gym.trainerworkload.dto.response.TrainerWorkloadResponse;
import com.gym.trainerworkload.service.TrainerWorkloadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/workloads")
@RequiredArgsConstructor
public class TrainerWorkloadController implements TrainerWorkloadControllerAPI {

    private final TrainerWorkloadService trainerWorkloadService;

    @PostMapping
    public void updateWorkload(@RequestBody WorkloadRequest request) {
        trainerWorkloadService.updateWorkload(request);
    }

    @GetMapping("/{username}")
    public TrainerWorkloadResponse getWorkload(@PathVariable String username) {
        return trainerWorkloadService.getTrainerWorkload(username);
    }

}