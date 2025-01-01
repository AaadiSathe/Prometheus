package com.smart.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.entity.User;
import com.smart.helper.Message;

@Controller
public class HomeController {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserRepository userRepository;

	@GetMapping("/home")
	public String home(Model model) {
		model.addAttribute("title", "Home");
		/*
		 * System.out.println("heeeee"); User user=new User();
		 * 
		 * user.setName("Kartik"); user.setEmail("Kartik@gmail.com");
		 * 
		 * Contact contact=new Contact(); user.getContact().add(contact);
		 * 
		 * userRepository.save(user);
		 */
		return "home";
	}

	@GetMapping("/about")
	public String about(Model model) {
		model.addAttribute("title", "About");
		return "about";
	}

	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title", "SignUp");
		model.addAttribute("user", new User());
		System.err.println("entr into singup");
		return "signup";
	}
	
	@RequestMapping(value = "/do_register", method = RequestMethod.POST)
	public String doRegister(@ModelAttribute("user") User user,
	                         @RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model,
	                         javax.servlet.http.HttpSession session) {
	    try {
	        if (!agreement) {
	            throw new Exception("Please agree to terms and conditions");
	        }
	        user.setRole("ROLE_USER");
	        user.setEnabled(true);
	        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
	        User result = userRepository.save(user);
	        model.addAttribute("user", result);
	        // Set success message in session
	        session.setAttribute("message", new Message("Registered successfully !!", "alert-success"));
	    } catch (Exception e) {
	        e.printStackTrace();
	        model.addAttribute("user", user);
	        // Set error message in session
	        session.setAttribute("message", new Message("Something went wrong !! " + e.getMessage(), "alert-danger"));
	        return "signup";
	    }
	    return "signup";
	}
	
	//custom handler login form	
	@GetMapping("/signin")
	public String customLogin() {
		
		return "signin";
	}
	

}
