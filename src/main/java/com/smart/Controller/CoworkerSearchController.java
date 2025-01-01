package com.smart.Controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smart.dao.UserRepository;
import com.smart.dao.addcoworkerRepository;
import com.smart.entity.Coworker;
import com.smart.entity.User;

@RestController
public class CoworkerSearchController {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
    private addcoworkerRepository addcoworkerRepository;
	
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable ("query")String query ,Principal principal )
	{
		System.out.println(query);
		User user = userRepository.getUserByUserName(principal.getName());
		List<Coworker> coworkers = this.addcoworkerRepository.findByNameContainingAndUser(query, user);
		
		
		return ResponseEntity.ok(coworkers);
		
	}
	
}
