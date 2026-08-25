package com.gym.trainerworkload.controller.api;

import com.gym.trainerworkload.dto.request.WorkloadRequest;
import com.gym.trainerworkload.dto.response.TrainerWorkloadResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Trainer Workload", description = "API for managing trainer workload hours")
public interface TrainerWorkloadControllerAPI {

    @Operation(summary = "Update trainer workload", description = "Adds or subtracts training duration from a trainer's workload")
    @ApiResponse(responseCode = "200", description = "Workload successfully updated")
    @ApiResponse(responseCode = "400", description = "Invalid request data")
    void updateWorkload(
            @Parameter(description = "Workload details", required = true)
            WorkloadRequest request
    );

    @Operation(summary = "Get trainer workload", description = "Retrieves the monthly and yearly workload summary for a specific trainer")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TrainerWorkloadResponse.class)))
    @ApiResponse(responseCode = "400", description = "Invalid username or missing data")
    TrainerWorkloadResponse getWorkload(
            @Parameter(description = "Username of the trainer", required = true)
            String username
    );
}