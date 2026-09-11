package com.gym.trainerworkload.listener;

import com.gym.trainerworkload.dto.request.WorkloadRequest;
import com.gym.trainerworkload.service.TrainerWorkloadService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WorkloadMessageListener {

    private final TrainerWorkloadService trainerWorkloadService;
    private final Validator validator;
    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = "workload.queue")
    public void receiveWorkloadRequest(@Payload WorkloadRequest request,
            @Header(value = "X-Transaction-Id", defaultValue = "UNKNOWN") String transactionId) {
        MDC.put("transactionId", transactionId);

        try {
            log.info("[Transaction: {}] Received message for trainer: {}", transactionId, request.getTrainerUsername());
            Set<ConstraintViolation<WorkloadRequest>> violations = validator.validate(request);

            if (!violations.isEmpty()) {
                log.error("[Transaction: {}] Invalid message received. Routing to DLQ. Violations: {}", transactionId, violations);
                jmsTemplate.convertAndSend("workload.dlq", request);
                return;
            }

            trainerWorkloadService.updateWorkload(request);
        } catch (Exception e) {
            log.error("[Transaction: {}] Error processing message. Routing to DLQ. Error: {}", transactionId, e.getMessage());
            jmsTemplate.convertAndSend("workload.dlq", request);
        } finally {
            MDC.remove("transactionId");
        }

    }

}