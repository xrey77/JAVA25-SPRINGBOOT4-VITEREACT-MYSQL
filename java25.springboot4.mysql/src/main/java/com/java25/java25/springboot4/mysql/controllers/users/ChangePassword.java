package com.java25.java25.springboot4.mysql.controllers.users;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.java25.java25.springboot4.mysql.services.KafkaProducerService;
import com.java25.java25.springboot4.mysql.services.KafkaProducerService.KafkaEventType;
import com.java25.java25.springboot4.mysql.services.UserService;

@RestController
@RequestMapping("/api")
public class ChangePassword {

	private final UserService userService;
	private final KafkaProducerService kafkaProducerService;	
	
	public ChangePassword(UserService userService, KafkaProducerService kafkaProducerService) {
		this.userService = userService;
		this.kafkaProducerService = kafkaProducerService;
	}
		
	@PatchMapping(path="/changepassword/{id}")
	public ResponseEntity<Map<String, String>>  changePassword(@RequestBody Map<String, Object> jsonInput, @PathVariable Long id) {
		
		Boolean idno = userService.findUserID(id);
		if (idno) {
			
	        Object newpassword = jsonInput.get("password");
			userService.changePassword(id, newpassword.toString());
			HashMap<String, String> map = new HashMap<>();
	        map.put("message", "You have changed your password successfully.");          	

			try {
				kafkaProducerService.sendEvent(KafkaEventType.CHANGE_PASSWORD, map);
			} catch (Exception e) {
				Map<String, String> errorMap = new HashMap<>();
				errorMap.put("message", "Failed to send Kafka event");
				errorMap.put("error", e.getMessage());
				
				return ResponseEntity.badRequest().body(errorMap); 					
			}				

	        return new ResponseEntity<>(map, HttpStatus.OK);
	        
		} else {
			HashMap<String, String> map = new HashMap<>();
	        map.put("message", "User ID not found.");          	
	        return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);         			
		}
		
	}
}