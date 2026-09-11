package com.gym.trainerworkload.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "trainer_summaries")
@CompoundIndex(name = "name_idx", def = "{'trainerFirstName': 1, 'trainerLastName': 1}")
public class TrainerSummary {

    @Id
    private String trainerUsername;
    private String trainerFirstName;
    private String trainerLastName;
    private boolean trainerStatus;

    @Builder.Default
    private List<YearSummary> years = new ArrayList<>();

}