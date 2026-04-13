package com.java25.java25.springboot4.mysql.controllers.users;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.java25.java25.springboot4.mysql.entities.User;
import com.java25.java25.springboot4.mysql.services.KafkaProducerService;
import com.java25.java25.springboot4.mysql.services.UserService;
import com.java25.java25.springboot4.mysql.services.KafkaProducerService.KafkaEventType;

@RestController
@RequestMapping("/api")
public class GetUserId {

	private final UserService userService;
	private final KafkaProducerService kafkaProducerService;	
			
	public GetUserId(UserService userService, KafkaProducerService kafkaProducerService) {
		this.userService = userService;
		this.kafkaProducerService = kafkaProducerService;		
	}
	
	@GetMapping(path="/getuserid/{id}")
	public ResponseEntity<Map<String, ?>> getUserById(@PathVariable Long id) {
		
		User user = userService.getUser(id);
		if (user != null) {
			
			  HashMap<String, Object> map = new HashMap<>();			  
			  map.put("id", user.getId());
			  map.put("firstname", user.getFirstname());
			  map.put("lastname", user.getLastname());
			  map.put("email", user.getEmail());
			  map.put("mobile", user.getMobile());
			  map.put("username", user.getUsername());
			  map.put("isactivated", user.getIsactivated());
			  map.put("userpic", user.getUserpic());
			  map.put("isblocked", user.getIsblocked());
			  map.put("mailtoken", user.getMailtoken());
			  map.put("qrcodeurl", user.getQrcodeurl());			  
	          map.put("message", "Your Details has successfully retrieved.");          
	          
			try {
				kafkaProducerService.sendEvent(KafkaEventType.GETUSER_BYID, map);
			} catch (Exception e) {
				Map<String, String> errorMap = new HashMap<>();
				errorMap.put("message", "Failed to send Kafka event");
				errorMap.put("error", e.getMessage());
				
				return ResponseEntity.badRequest().body(errorMap); 					
			}				
	          
				return ResponseEntity.ok(map);    
			
		} else {
			
		  HashMap<String, String> map = new HashMap<>();
          map.put("message", "User ID not found.");          	
    	  return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);              	  
          
		}		
	}
}