package com.java25.java25.springboot4.mysql.services;

import java.util.HashSet;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.java25.java25.springboot4.mysql.dto.RegisterDto;
import com.java25.java25.springboot4.mysql.entities.Role;
import com.java25.java25.springboot4.mysql.entities.User;
import com.java25.java25.springboot4.mysql.repository.RoleRepository;
import com.java25.java25.springboot4.mysql.repository.UserRepository;

@Service
public class AuthService {

	@Autowired
	private final PasswordEncoder passwordEncoder;
	
	@Autowired
	private final UserRepository userRepository;

	@Autowired
	private final RoleRepository roleRepository;
	
	@Autowired
	private final ModelMapper modelMapper;
	
	
    @Autowired
    public AuthService(
    		PasswordEncoder passwordEncoder,
    		UserRepository userRepository,
    		RoleRepository roleRepository,
    		ModelMapper modelMapper) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;         
    }	
        	
    public User registerUser(RegisterDto registerDto) {

    	String encodedPassword = passwordEncoder.encode(registerDto.getPassword());
        registerDto.setPassword(encodedPassword);
        
        User user = new User();        
        modelMapper.map(registerDto, user);
        
        Role userRole = roleRepository.findById(2)
        	    .orElseThrow(() -> new RuntimeException("Error: Role not found."));
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);        
        
        user.setFirstname(registerDto.getFirstname());
        user.setLastname(registerDto.getLastname());
        user.setEmail(registerDto.getEmail());
        user.setMobile(registerDto.getMobile());
        user.setUsername(registerDto.getUsername());
        user.setPassword(registerDto.getPassword());   
        user.setUserpic("pix.png");       
        User registeredUser = userRepository.save(user);        
        return registeredUser;    	
    }        
    

    public Boolean getUserEmail(String emailadd) {
    	return userRepository.existsByEmail(emailadd);
    }
        
    public Boolean getUserInfo(String username) {
    	return userRepository.existsByUsername(username);
    	
    }
    
    
}
