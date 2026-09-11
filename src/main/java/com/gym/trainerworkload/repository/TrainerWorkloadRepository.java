package com.gym.trainerworkload.repository;

import com.gym.trainerworkload.model.TrainerSummary;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainerWorkloadRepository extends MongoRepository<TrainerSummary, String> {

    Optional<TrainerSummary> findByTrainerUsername(String trainerUsername);

}