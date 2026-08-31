package com.gym.trainerworkload.listener;

import com.gym.trainerworkload.dto.request.WorkloadRequest;
import com.gym.trainerworkload.service.TrainerWorkloadService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WorkloadMessageListener {

    private final TrainerWorkloadService trainerWorkloadService;
    private final Validator validator;
    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = "workload.queue")
    public void receiveWorkloadRequest(WorkloadRequest request) {
        Set<ConstraintViolation<WorkloadRequest>> violations = validator.validate(request);

        if (!violations.isEmpty()) {
            log.error("Invalid message received. Routing to DLQ. Violations: {}", violations);
            jmsTemplate.convertAndSend("workload.dlq", request);
            return;
        }

        try {
            trainerWorkloadService.updateWorkload(request);
        } catch (Exception e) {
            log.error("Error processing message. Routing to DLQ. Error: {}", e.getMessage());
            jmsTemplate.convertAndSend("workload.dlq", request);
        }
    }
}