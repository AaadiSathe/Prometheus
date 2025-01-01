package com.smart.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smart.dao.UserRepository;
import com.smart.entity.User;

public class UserDetailServiceImp implements UserDetailsService {
 
	@Autowired
    private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//fetchin user  from data base
	User user=	userRepository.getUserByUserName(username);
		
		if(user==null) {
			throw new UsernameNotFoundException("user not found of email");
		}
		
		CustomUserDetail customUserDetail=new CustomUserDetail(user);
		
		return customUserDetail;
	}

}
