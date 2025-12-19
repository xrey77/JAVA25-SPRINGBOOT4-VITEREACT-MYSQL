package com.java25.java25.springboot4.mysql.services;

//import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.jwt.JwtClaimsSet;
//import org.springframework.security.oauth2.jwt.JwtDecoder;
//import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import com.java25.java25.springboot4.mysql.dto.ProfileDto;
import com.java25.java25.springboot4.mysql.dto.UserlistDto;
import com.java25.java25.springboot4.mysql.entities.User;
import com.java25.java25.springboot4.mysql.repository.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class UserService {

	private UserRepository userRepository;	
	private final ModelMapper modelMapper;
	private final PasswordEncoder passwordEncoder;
	
	@Autowired
	public UserService(
			UserRepository userRepository,
			TwoFactorAuthService twoFactorAuthService,
			PasswordEncoder passwordEncoder,
			ModelMapper modelMapper) {
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
		this.passwordEncoder = passwordEncoder;
	}
			
	
	
    public User getUser(Integer id) {
    	User user = userRepository.findById(id).orElse(null);
        return user;    		
    }
    
    public List<UserlistDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserlistDto> userlistDto = users.stream()
                .map(user -> modelMapper.map(user, UserlistDto.class))
                .collect(Collectors.toList());
        return userlistDto;
    }        
    
    public User getUserName(String username) {
    	return userRepository.findByUsername(username);
    	
    }
    
    public Boolean findUserID(Integer id) {
        return userRepository.findById(id).isPresent();
    }
            
    
    @Transactional
    public User changePassword(Integer id, String newpassword) {
    	String encodedPassword = passwordEncoder.encode(newpassword);

    	User pwdToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    	pwdToUpdate.setPassword(encodedPassword);
    	return userRepository.save(pwdToUpdate);
    }
	
    public User updateProfilepic(Integer id, String newfile) {
    	User pictureToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    	pictureToUpdate.setUserpic(newfile);
    	return userRepository.save(pictureToUpdate);
    	
    }
    
    public User enableMfa(Integer id, String secret, String qrcodebase64) {
    	User mfaToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    	mfaToUpdate.setSecret(secret);
    	mfaToUpdate.setQrcodeurl(qrcodebase64);
    	return userRepository.save(mfaToUpdate);
    	
    }
    
    public User disableMfa(Integer id) {
    	User mfaToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    	mfaToUpdate.setSecret(null);
    	mfaToUpdate.setQrcodeurl(null);
    	return userRepository.save(mfaToUpdate);    	
    }
    
    public User updateUserProfile(Integer id, ProfileDto profileDtls) {
    	
    	User profileToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    	
        modelMapper.map(profileDtls, profileToUpdate);
        User updatedUser = userRepository.save(profileToUpdate);        
        return updatedUser;    	
    	
    	
    }
    
}
