package com.java25.java25.springboot4.mysql.controllers.users;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.java25.java25.springboot4.mysql.dto.UserlistDto;
import com.java25.java25.springboot4.mysql.services.KafkaProducerService;
import com.java25.java25.springboot4.mysql.services.UserService;
import com.java25.java25.springboot4.mysql.services.KafkaProducerService.KafkaEventType;

@RestController
@RequestMapping("/api")
public class GetAllUsers {

	private final UserService userService;
	private final KafkaProducerService kafkaProducerService;	
		
	public GetAllUsers(UserService userService, KafkaProducerService kafkaProducerService) {
		this.userService = userService;
		this.kafkaProducerService = kafkaProducerService;				
	}
	
	@GetMapping(path="/getusers")
	public ResponseEntity<?>  getUsers() {
				
		List<UserlistDto> users = userService.getAllUsers();
		try {
			kafkaProducerService.sendEvent(KafkaEventType.GETALL_USERS, users);
		} catch (Exception e) {
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("message", "Failed to send Kafka event");
            errorMap.put("error", e.getMessage());
            
            return ResponseEntity.badRequest().body(errorMap); 			
		}
		return ResponseEntity.ok(users);		
		
	}
			
}