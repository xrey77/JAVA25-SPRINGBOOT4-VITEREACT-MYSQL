package com.java25.java25.springboot4.mysql.services;

import com.java25.java25.springboot4.mysql.dto.CentralizedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private static final Logger log = LoggerFactory.getLogger(KafkaProducerService.class);
    private static final String CENTRALIZED_TOPIC = "api-events-topic";

    public enum KafkaEventType {
        CREATE_USER,
        SIGNIN_USER,
        GETUSER_BYID,
        GETALL_USERS,
        CHANGE_PASSWORD,
        UPDATE_PROFILE,
        UPLOAD_PICTURE,
        ACTIVATE_MFA,
        VERIFY_OTP,
        PDF_REPORT,
        PRODUCT_SEARCH,
        PRODUCTS_LIST,
        SALES_CHART
    }

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(KafkaEventType eventType, Object payload) {
        try {
            log.info("Processing {} event...", eventType);
            // kafkaTemplate.send(CENTRALIZED_TOPIC, eventType.name(), payload);
            
            CentralizedEvent<Object> event = new CentralizedEvent<Object>(eventType, payload);
            
            kafkaTemplate.send(CENTRALIZED_TOPIC, eventType.name(), event);
            
        } catch (Exception e) {
            log.error("Failed to send message to Kafka for event {}: {}", eventType, e.getMessage(), e);
            throw e;
        }
    }
}
