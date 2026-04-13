package com.java25.java25.springboot4.mysql.controllers.userAccess;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.java25.java25.springboot4.mysql.dto.RegisterDto;
import com.java25.java25.springboot4.mysql.services.AuthService;
import com.java25.java25.springboot4.mysql.services.KafkaProducerService;
import com.java25.java25.springboot4.mysql.services.KafkaProducerService.KafkaEventType;

import org.springframework.kafka.core.KafkaTemplate;

@RestController
@RequestMapping("/auth")
public class RegisterController {

	private final AuthService authService;
    private final KafkaProducerService kafkaProducerService;

	public RegisterController(AuthService authService, KafkaProducerService kafkaProducerService) {
		this.authService = authService;
        this.kafkaProducerService = kafkaProducerService;
	}
			
    @PostMapping(path="/signup")	
	public ResponseEntity<Map<String, String>> signUp(@RequestBody RegisterDto user) {
    	
    	Boolean userEmail = authService.getUserEmail(user.getEmail());
    	if (userEmail) {
            HashMap<String, String> map = new HashMap<>();
            map.put("message", "Email Address is already taken.");
            map.put("username", user.getEmail());
            return new ResponseEntity<>(map, HttpStatus.CONFLICT);             		
    	}
    	
    	Boolean userName = authService.getUserInfo(user.getUsername());
    	if (userName) {
            HashMap<String, String> map = new HashMap<>();
            map.put("message", "Username is already taken.");        	
            return new ResponseEntity<>(map, HttpStatus.CONFLICT);             		    		
    	}
    	
    	authService.registerUser(user);

		try {
            kafkaProducerService.sendEvent(KafkaEventType.CREATE_USER, user);            
        } catch (Exception e) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("message", "Failed to send Kafka event");
            errorMap.put("error", e.getMessage());
            
            return ResponseEntity.badRequest().body(errorMap);             
        }
		
        HashMap<String, String> map = new HashMap<>();
        map.put("message", "You have registered successfully, please login now.");
        return new ResponseEntity<>(map, HttpStatus.CREATED);         
	}
}