package com.java25.java25.springboot4.mysql.services;

import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.java25.java25.springboot4.mysql.dto.RegisterDto;
import com.java25.java25.springboot4.mysql.entities.Role;
import com.java25.java25.springboot4.mysql.entities.User;
import com.java25.java25.springboot4.mysql.repository.RoleRepository;
import com.java25.java25.springboot4.mysql.repository.UserMapper;
import com.java25.java25.springboot4.mysql.repository.UserRepository;

@Service
public class AuthService {    
	@Autowired
	private final PasswordEncoder passwordEncoder;
	
	@Autowired
	private final UserRepository userRepository;

	@Autowired
	private final RoleRepository roleRepository;	
    private final UserMapper userMapper;	
	
	
    @Autowired
    public AuthService(
    		UserMapper userMapper,
    		PasswordEncoder passwordEncoder,
    		UserRepository userRepository,
    		RoleRepository roleRepository
    		) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;        
    }	
        	
    public User registerUser(RegisterDto registerDto) {

    	String encodedPassword = passwordEncoder.encode(registerDto.getPassword());
        registerDto.setPassword(encodedPassword);
        
        User user = new User();        
        userMapper.updateUserFromDto(registerDto, user);
        
        Role userRole = roleRepository.findById((long) 2)
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
        user.setIsactivated(1);
        user.setRole_id(2);
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
