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
import com.java25.java25.springboot4.mysql.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class LoginController {

	private final UserService userService;	
	private JwtService jwtService;	
	private PasswordEncoder passwordEncoder;
	
	public LoginController(
			PasswordEncoder passwordEncoder,
			JwtService jwtService,
			UserService userService) {
		this.userService = userService;
		this.jwtService = jwtService;
		this.passwordEncoder = passwordEncoder;
	}
	
    @PostMapping(path="/signin")	
	public ResponseEntity<Map<String, String>> signIn(
			HttpServletRequest request, 
			HttpServletResponse response,
			@RequestBody LoginDto loginDto) {
    	
//    	try {
//	      Authentication authentication = authManager.authenticate(
//	   		        new UsernamePasswordAuthenticationToken(
//	   		            loginDto.getUsername(), loginDto.getPassword())
//	       );
//	      		  
//		   String token = jwtService.generateToken(authentication);      		  
//	       return ResponseEntity.ok(Map.of("token", token));
//      
//    	} catch (AuthenticationException e) {
//    	    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//    	}
       
//      return ResponseEntity.ok(Map.of("message", "Login Successfull."));         		        		  

    	
    	
      User user = userService.getUserName(loginDto.getUsername());          

      if (user != null) {
      	  if (passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {


      		  
      		  String token = jwtService.generateToken2(user);      		  
      		  
  			  String idno = String.valueOf(user.getId());
  			  String isactivated = String.valueOf(user.getIsactivated());
  			  String isblocked = String.valueOf(user.getIsblocked());
              HashMap<String, String> map = new HashMap<>();
              map.put("message", "You have logged-in successfully.");
              map.put("id", idno);
              map.put("firstname", user.getFirstname());
              map.put("lastname", user.getLastname());
              map.put("email", user.getEmail());
              map.put("mobile", user.getMobile());
              map.put("username", user.getUsername());
              map.put("isactivated", isactivated);
              map.put("isblocked", isblocked);
              map.put("userpic", user.getUserpic());
              map.put("qrcodeurl", user.getQrcodeurl());
              map.put("token", token);
              
              return new ResponseEntity<>(map, HttpStatus.OK);               		        		  
      		  
      	  } else {

      		  HashMap<String, String> map = new HashMap<>();
              map.put("message", "Invalid password, please try again.");          	
              return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);         
      		  
      	  }
      	  
      } else {
    	  
  		  HashMap<String, String> map = new HashMap<>();
          map.put("message", "Username not found, please register.");          	
          return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);         
    	  
      }
    	
	}

}

