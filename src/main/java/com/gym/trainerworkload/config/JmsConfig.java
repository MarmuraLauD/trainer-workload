package com.gym.trainerworkload.config;

import com.gym.trainerworkload.dto.request.WorkloadRequest;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.JacksonJsonMessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@Configuration
public class JmsConfig {

    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        JacksonJsonMessageConverter converter = new JacksonJsonMessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");

        Map<String, Class<?>> typeMappings = new HashMap<>();
        typeMappings.put("com.gym.crmspringboot.dto.request.WorkloadRequest", WorkloadRequest.class);
        converter.setTypeIdMappings(typeMappings);

        converter.setTrustedPackages(
                "com.gym.crmspringboot.dto.request",
                "com.gym.trainerworkload.dto.request",
                "java.util",
                "java.lang"
        );
        return converter;

    }

}