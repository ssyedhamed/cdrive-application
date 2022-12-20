package com.ssyedhamed.cdrive.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.ssyedhamed.cdrive.dao.UserRepository;
import com.ssyedhamed.cdrive.entities.User;

public class CustomUserDetailsService implements UserDetailsService{
	@Autowired
	private UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.getUserByUserName(username);
		if(user==null) {
			throw new UsernameNotFoundException("User not found");
		}
		CustomUserDetails userDetails=new CustomUserDetails(user);
		return userDetails;
	}

}
