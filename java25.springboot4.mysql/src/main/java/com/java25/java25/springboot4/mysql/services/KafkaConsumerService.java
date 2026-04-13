package com.java25.java25.springboot4.mysql.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.java25.java25.springboot4.mysql.entities.User;
import com.java25.java25.springboot4.mysql.entities.Product;
import com.java25.java25.springboot4.mysql.services.KafkaProducerService.KafkaEventType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class KafkaConsumerService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "api-events-topic", groupId = "central_consumer_group")
    public void consumeCentralizedEvents(String messageJson) {
        try {
            JsonNode rootNode = objectMapper.readTree(messageJson);
            
            String eventTypeStr = rootNode.get("eventType").asText();
            KafkaEventType eventType = KafkaEventType.valueOf(eventTypeStr);
            
            JsonNode payloadNode = rootNode.get("payload");
            
            switch (eventType) {
                case GETALL_USERS:
                    List<User> users = objectMapper.convertValue(
                        payloadNode, 
                        new TypeReference<List<User>>() {}
                    );
                    processUsers(users);
                    break;
                    
               case PRODUCTS_LIST:
                   List<Product> products = objectMapper.convertValue(
                       payloadNode, 
                       new TypeReference<List<Product>>() {}
                   );
                   processProducts(products);
                   break;
                    
                default:
                    System.out.println("Unhandled event type: " + eventType);
                    break;
            }
            
        } catch (Exception e) {
            System.err.println("Error processing centralized message: " + e.getMessage());
        }
    }

    private void processUsers(List<User> users) {
        System.out.println("Successfully processed users list of size: " + users.size());
    }

   private void processProducts(List<Product> products) {
       System.out.println("Successfully processed products list of size: " + products.size());
   }
}
