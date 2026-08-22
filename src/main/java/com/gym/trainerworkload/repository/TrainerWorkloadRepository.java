package com.gym.trainerworkload.repository;

import com.gym.trainerworkload.dto.request.WorkloadRequest;
import com.gym.trainerworkload.model.TrainerSummary;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class TrainerWorkloadRepository {

    private final Map<String, TrainerSummary> storage = new ConcurrentHashMap<>();

    public TrainerSummary getOrCreateTrainer(String username, WorkloadRequest request) {
        return storage.computeIfAbsent(username, _ ->
            TrainerSummary.builder()
                    .trainerUsername(request.getTrainerUsername())
                    .trainerFirstName(request.getTrainerFirstName())
                    .trainerLastName(request.getTrainerLastName())
                    .trainerStatus(request.isActive())
                    .years(new ConcurrentHashMap<>())
                    .build()
        );
    }

    public Optional<TrainerSummary> getTrainer(String username) {
        return Optional.ofNullable(storage.get(username));
    }

}
