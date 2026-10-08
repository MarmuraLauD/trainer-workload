package com.gym.trainerworkload.cucumber.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gym.trainerworkload.dto.request.WorkloadRequest;
import com.gym.trainerworkload.dto.response.ErrorResponse;
import com.gym.trainerworkload.dto.response.TrainerWorkloadResponse;
import com.gym.trainerworkload.model.ActionType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TrainerWorkloadSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private WorkloadRequest workloadRequest;
    private ResultActions resultActions;

    @Given("a valid workload request for trainer {string} with action type {string}")
    public void aValidWorkloadRequestForTrainerWithActionType(String username, String actionType) {
        workloadRequest = WorkloadRequest.builder()
                .trainerUsername(username)
                .trainerFirstName("TestFirstName")
                .trainerLastName("TestLastName")
                .isActive(true)
                .trainingDate(LocalDate.now())
                .trainingDuration(60)
                .actionType(ActionType.valueOf(actionType))
                .build();
    }

    @Given("the workload is updated via POST to {string}")
    public void theWorkloadIsUpdatedViaPOSTTo(String endpoint) throws Exception {
        mockMvc.perform(post(endpoint)
                        .with(user("trainer_user").roles("TRAINER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(workloadRequest)))
                .andExpect(status().isOk());
    }

    @When("a POST request is made to update workload at {string}")
    public void aPostRequestIsMadeToUpdateWorkloadAt(String endpoint) throws Exception {
        resultActions = mockMvc.perform(post(endpoint)
                .with(user("trainer_user").roles("TRAINER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(workloadRequest)));
    }

    @When("a GET request is made to fetch workload at {string}")
    public void aGetRequestIsMadeToFetchWorkloadAt(String endpoint) throws Exception {
        resultActions = mockMvc.perform(get(endpoint)
                .with(user("trainer_user").roles("TRAINER")));
    }

    @Then("the workload response status should be {int}")
    public void theWorkloadResponseStatusShouldBe(int expectedStatus) throws Exception {
        resultActions.andExpect(status().is(expectedStatus));
    }

    @And("the response should contain the correct workload summary for {string}")
    public void theResponseShouldContainTheCorrectWorkloadSummaryFor(String username) throws Exception {
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        TrainerWorkloadResponse response = objectMapper.readValue(responseBody, TrainerWorkloadResponse.class);

        assertNotNull(response);
        assertEquals(username, response.getTrainerUsername());
        assertNotNull(response.getYears());
    }

    @And("the response should contain a generic error message")
    public void theResponseShouldContainAGenericErrorMessage() throws Exception {
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        ErrorResponse response = objectMapper.readValue(responseBody, ErrorResponse.class);

        assertNotNull(response);
        assertEquals(400, response.status());
        assertEquals("An error occurred while processing the request. Please verify your data.", response.message());
    }
}