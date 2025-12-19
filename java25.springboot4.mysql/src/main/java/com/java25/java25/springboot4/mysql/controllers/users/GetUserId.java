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
import com.java25.java25.springboot4.mysql.services.UserService;

@RestController
@RequestMapping("/api")
public class GetUserId {

	private final UserService userService;
		
	public GetUserId(UserService userService) {
		this.userService = userService;		
	}
	
	@GetMapping(path="/getuserid/{id}")
	public ResponseEntity<Map<String, String>> getUserById(@PathVariable Integer id) {
		
		User user = userService.getUser(id);
		if (user != null) {
			
			  HashMap<String, String> map = new HashMap<>();
			  
  			  String idno = String.valueOf(user.getId());
  			  String isactivated = String.valueOf(user.getIsactivated());
  			  String isblocked = String.valueOf(user.getIsblocked());
  			  String mailtoken = String.valueOf(user.getMailtoken());
			  map.put("id", idno);
			  map.put("firstname", user.getFirstname());
			  map.put("lastname", user.getLastname());
			  map.put("email", user.getEmail());
			  map.put("mobile", user.getMobile());
			  map.put("username", user.getUsername());
			  map.put("isactivated", isactivated);
			  map.put("userpic", user.getUserpic());
			  map.put("isblocked", isblocked);
			  map.put("mailtoken", mailtoken);
			  map.put("qrcodeurl", user.getQrcodeurl());			  
	          map.put("message", "Your Details has successfully retrieved.");          	
	          return new ResponseEntity<>(map, HttpStatus.OK);         
			
		} else {
			
		  HashMap<String, String> map = new HashMap<>();
          map.put("message", "User ID not found.");          	
          return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
          
		}		
	}
}