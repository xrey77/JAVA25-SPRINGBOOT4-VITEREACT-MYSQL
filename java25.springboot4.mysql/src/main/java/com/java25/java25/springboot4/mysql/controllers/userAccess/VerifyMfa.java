package com.java25.java25.springboot4.mysql.controllers.userAccess;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.java25.java25.springboot4.mysql.dto.MfaToken;
import com.java25.java25.springboot4.mysql.entities.User;
import com.java25.java25.springboot4.mysql.services.UserService;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class VerifyMfa {

	private final UserService userService;
	
	public VerifyMfa(UserService userService) {
		this.userService = userService;
	}
		
    @PatchMapping(path="/mfa/verifytotp/{id}")	
    public ResponseEntity<Map<String, String>> mfaVerifyTotp(@RequestBody MfaToken mfaToken, @PathVariable Integer id) {    	

		User user = userService.getUser(id);
		if (user != null) {
		
			if (user.getSecret() == null) {
		        HashMap<String, String> map = new HashMap<>();
		        map.put("message", "Multi-Factor Authenticator is not yet enabled.");	        
		        return new ResponseEntity<>(map, HttpStatus.CONFLICT);         	        					
			}
			
	        String otpcode = mfaToken.getOtp();
	        Integer otp = Integer.parseInt(otpcode);
	        String secret = user.getSecret();
	        GoogleAuthenticator gAuth = new GoogleAuthenticator();
	        boolean isCodeValid = gAuth.authorize(secret, otp);
	        if (isCodeValid) {
	        	
		        HashMap<String, String> map = new HashMap<>();
		        map.put("username", user.getUsername());
		        map.put("message", "OTP code has been verified successfully.");	        
		        return new ResponseEntity<>(map, HttpStatus.OK);         
	        	
	        } else {
	        	
		        HashMap<String, String> map = new HashMap<>();
		        map.put("message", "Invalid OTP code, please try again.");	        
		        return new ResponseEntity<>(map, HttpStatus.CONFLICT);         	        	
	        }
	        			
		} else {
	        
	        HashMap<String, String> map = new HashMap<>();
	        map.put("message", "User ID not found.");	        
	        return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);         
						
		}
	}		
}