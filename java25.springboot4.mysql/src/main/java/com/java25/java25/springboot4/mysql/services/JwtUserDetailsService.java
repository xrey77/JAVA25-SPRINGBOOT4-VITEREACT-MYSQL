package com.java25.java25.springboot4.mysql.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.java25.java25.springboot4.mysql.entities.User;
import com.java25.java25.springboot4.mysql.repository.UserRepository;

@Service
@Primary
public class JwtUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User users = repository.findByUsername(username);
		if (users == null) {
			    throw new UsernameNotFoundException("User not found with username");			 
		}
	    return users;			 		 
	}	
}