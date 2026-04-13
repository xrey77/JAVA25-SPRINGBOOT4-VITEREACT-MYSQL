package com.java25.java25.springboot4.mysql.dto;

import com.java25.java25.springboot4.mysql.services.KafkaProducerService.KafkaEventType;

public record CentralizedEvent<T>(
    KafkaEventType eventType,
    T payload
) {
}
