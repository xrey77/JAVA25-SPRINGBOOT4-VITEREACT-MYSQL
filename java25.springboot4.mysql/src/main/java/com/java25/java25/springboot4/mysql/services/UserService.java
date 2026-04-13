package com.java25.java25.springboot4.mysql.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.java25.java25.springboot4.mysql.dto.ProfileDto;
import com.java25.java25.springboot4.mysql.dto.UserlistDto;
import com.java25.java25.springboot4.mysql.entities.User;
import com.java25.java25.springboot4.mysql.repository.UserMapper;
import com.java25.java25.springboot4.mysql.repository.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class UserService {

	private final UserRepository userRepository;	
	private final PasswordEncoder passwordEncoder;	
    private final UserMapper userMapper;	
		
	@Autowired
	public UserService(
    		UserMapper userMapper,			
			UserRepository userRepository,
			PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;        		
	}
					
    public User getUser(Long id) {
    	User user = userRepository.findById(id).orElse(null);
        return user;    		
    }
    
    public List<UserlistDto> getAllUsers() {
        List<User> users = userRepository.findAll();        
        return userMapper.toDtoList(users);    	
    }        
    
    public User getUserName(String username) {
    	return userRepository.findByUsername(username);
    	
    }
    
    public Boolean findUserID(Long id) {
        return userRepository.findById(id).isPresent();
    }
            
    
    @Transactional
    public User changePassword(Long id, String newpassword) {
    	String encodedPassword = passwordEncoder.encode(newpassword);

    	User pwdToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    	pwdToUpdate.setPassword(encodedPassword);
    	return userRepository.save(pwdToUpdate);
    }
	
    public User updateProfilepic(Long id, String newfile) {
    	User pictureToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    	pictureToUpdate.setUserpic(newfile);
    	return userRepository.save(pictureToUpdate);
    	
    }
    
    public User enableMfa(Long id, String secret, String qrcodebase64) {
    	User mfaToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    	mfaToUpdate.setSecret(secret);
    	mfaToUpdate.setQrcodeurl(qrcodebase64);
    	return userRepository.save(mfaToUpdate);
    	
    }
    
    public User disableMfa(Long id) {
    	User mfaToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    	mfaToUpdate.setSecret(null);
    	mfaToUpdate.setQrcodeurl(null);
    	return userRepository.save(mfaToUpdate);    	
    }
    
    public User updateUserProfile(Long id, ProfileDto profileDtls) {
        User profileToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    	
        userMapper.updateUserFromProfileDto(profileDtls, profileToUpdate);        
        return userRepository.save(profileToUpdate);    	    	    	    	
    }    
}
