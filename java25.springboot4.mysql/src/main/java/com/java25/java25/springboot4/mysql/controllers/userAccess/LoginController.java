package com.java25.java25.springboot4.mysql.controllers.userAccess;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.java25.java25.springboot4.mysql.dto.LoginDto;
import com.java25.java25.springboot4.mysql.entities.User;
import com.java25.java25.springboot4.mysql.services.JwtService;
import com.java25.java25.springboot4.mysql.services.KafkaProducerService;
import com.java25.java25.springboot4.mysql.services.KafkaProducerService.KafkaEventType;
import com.java25.java25.springboot4.mysql.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class LoginController {

	private final UserService userService;	
	private final JwtService jwtService;	
	private final PasswordEncoder passwordEncoder;
	private final KafkaProducerService kafkaProducerService;	
	
	public LoginController(
			KafkaProducerService kafkaProducerService,	
			PasswordEncoder passwordEncoder,
			JwtService jwtService,
			UserService userService) {
		this.userService = userService;
		this.jwtService = jwtService;
		this.passwordEncoder = passwordEncoder;
		this.kafkaProducerService = kafkaProducerService;
	}
	
    @PostMapping(path="/signin")	
	public ResponseEntity<Map<String, ?>> signIn(			
			HttpServletRequest request, 
			HttpServletResponse response,
			@RequestBody LoginDto loginDto) {
    	    	
      User user = userService.getUserName(loginDto.getUsername());          

      if (user != null) {
      	  if (passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
      		  
				String token = jwtService.generateToken2(user);
				HashMap<String, Object> map = new HashMap<>();
				map.put("message", "You have logged-in successfully.");
				map.put("id", user.getId());
				map.put("firstname", user.getFirstname());
				map.put("lastname", user.getLastname());
				map.put("email", user.getEmail());
				map.put("mobile", user.getMobile());
				map.put("username", user.getUsername());
				map.put("isactivated", user.getIsactivated());
				map.put("isblocked", user.getIsblocked());
				map.put("userpic", user.getUserpic());
				map.put("qrcodeurl", user.getQrcodeurl());
				map.put("token", token);
              
				try {
					kafkaProducerService.sendEvent(KafkaEventType.SIGNIN_USER, map);
				} catch (Exception e) {
	                Map<String, String> errorMap = new HashMap<>();
	                errorMap.put("message", "Failed to send Kafka event");
	                errorMap.put("error", e.getMessage());
	                
	                return ResponseEntity.badRequest().body(errorMap); 					
				}				
				return ResponseEntity.ok(map);      		  
      	  } else {

      		  HashMap<String, String> map = new HashMap<>();
              map.put("message", "Invalid password, please try again.");          	
              return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);               		  
      	  }
      	  
      } else {
  		  HashMap<String, String> map = new HashMap<>();
          map.put("message", "Username not found, please register.");
    	  return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);              	  
      }    	
	}
}