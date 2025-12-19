package com.java25.java25.springboot4.mysql.controllers.users;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.java25.java25.springboot4.mysql.entities.User;
import com.java25.java25.springboot4.mysql.services.FileStorageService;
import com.java25.java25.springboot4.mysql.services.UserService;

@RestController
@RequestMapping("/api")
public class UploadPicture {

	private final UserService userService;	
    private final FileStorageService fileStorageService;
	
	public UploadPicture(
			FileStorageService fileStorageService,
			UserService userService) {
		this.userService = userService;
		this.fileStorageService = fileStorageService;
	}
		
	@PatchMapping(path="/uploadpicture/{id}")
	public ResponseEntity<Map<String, String>>  uploadProfilePicture(
			@RequestParam("userpic") MultipartFile file,
			@PathVariable Integer id) throws java.io.IOException {
		
		User user = userService.getUser(id);
		if (user != null) {
			 String oldpic = user.getUserpic();
	         String fileName = fileStorageService.storeFile(file, id, oldpic);	            
	         userService.updateProfilepic(id, fileName);
	         
	         HashMap<String, String> map = new HashMap<>();
		     map.put("message", "You have change your profile picture successfully.");  
		     map.put("userpic", user.getUserpic());
		     return new ResponseEntity<>(map, HttpStatus.OK);         			
	            			
		} else {
			
			HashMap<String, String> map = new HashMap<>();
	        map.put("message", "User ID not found.");          	
	        return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);         			
			
		}
	}		
}