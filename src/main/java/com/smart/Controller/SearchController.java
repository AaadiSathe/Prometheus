package com.smart.Controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.smart.dao.CityRepositoy;
import com.smart.dao.UserRepository;
import com.smart.dao.addcustomerRepository;
import com.smart.dao.contactRepository;
import com.smart.entity.Addcustomer;
import com.smart.entity.Cities;
import com.smart.entity.Contact;
import com.smart.entity.Countries;
import com.smart.entity.User;

@RestController //this is use to show response body
public class SearchController {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private contactRepository contactRepository;
	
	@Autowired
	private CityRepositoy cityRepositoy;
	
	
	
	@Autowired
	private  addcustomerRepository cuAddcustomerRepository;
	
	//search Handler
	@GetMapping("/serach/{query}")
	public ResponseEntity<?> search(@PathVariable("query") String query, Principal principal){
		
		
		User user=this.userRepository.getUserByUserName(principal.getName());
		List<Contact> contact=this.contactRepository.findByNameContainingAndUser(query, user);
		return ResponseEntity.ok(contact);
		
	}
	
	@GetMapping("/hello")
	public List<Addcustomer> helloWorld() {
		
	    return cuAddcustomerRepository.findAll();
	}
	
	
	@GetMapping("/userinfo")
	public List<User> userinfo() {
		
	    return userRepository.findAll();
	}
	
	@GetMapping("/hello/{id}")
	public Addcustomer helloWorld(@PathVariable("id") int id) {
		
		Countries country=new Countries();
		country.setId(10);
		country.setName("India");
		country.setPhoneCode(23);
		country.setShortName("IN");
		
	    return cuAddcustomerRepository.findCustomerByUserUsingId(1);
	}
	
	
	
	
	


}
