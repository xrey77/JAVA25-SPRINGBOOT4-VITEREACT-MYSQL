package com.java25.java25.springboot4.mysql.controllers.users;

import java.io.IOException;
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
import com.java25.java25.springboot4.mysql.services.KafkaProducerService;
import com.java25.java25.springboot4.mysql.services.KafkaProducerService.KafkaEventType;
import com.java25.java25.springboot4.mysql.services.UserService;

@RestController
@RequestMapping("/api")
public class UploadPicture {

    private final UserService userService;	
    private final FileStorageService fileStorageService;
    private final KafkaProducerService kafkaProducerService;	
	
    public UploadPicture(
            KafkaProducerService kafkaProducerService,
            FileStorageService fileStorageService,
            UserService userService) {
        this.kafkaProducerService = kafkaProducerService;				
        this.userService = userService;
        this.fileStorageService = fileStorageService;
    }
		
    @PatchMapping(path = "/uploadpicture/{id}")
    public ResponseEntity<Map<String, String>> uploadProfilePicture(
            @RequestParam("userpic") MultipartFile file,
            @PathVariable Long id) throws IOException {
		
        User user = userService.getUser(id);
        if (user == null) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("message", "User ID not found.");          	
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMap);
        }

        String oldPic = user.getUserpic();
        String fileName = fileStorageService.storeFile(file, id, oldPic);	            
        
        // Persist change to the DB
        userService.updateProfilepic(id, fileName);

        Map<String, String> map = new HashMap<>();
        map.put("message", "You have changed your profile picture successfully.");  
        map.put("userpic", fileName); // Point of fix: Using the updated filename directly

        // Kafka operations are generally better off handled asynchronously 
        // inside the Service layer rather than the Controller.
        kafkaProducerService.sendEvent(KafkaEventType.UPLOAD_PICTURE, map);

        return ResponseEntity.ok(map);         								  
    }
}
