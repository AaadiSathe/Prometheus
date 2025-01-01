package com.smart.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.smart.dao.CityRepositoy;

import com.smart.dao.StateRepository;


@Controller
@RequestMapping("/drop")
public class DropDown {
	
	@Autowired
	private StateRepository stateRepository;
	@Autowired
	private CityRepositoy cityRepository;
	
	
	@GetMapping("/state/{id}")
	public String addState(@PathVariable("id") Integer id,Model model) {
		System.err.println("State"+id);
		model.addAttribute("stateList", stateRepository.getAllState(id));

		return "normal/add_contact_form";
	}
	
	@GetMapping("/city/{id}")
	public String addCity(@PathVariable("id") Integer id,Model model) {
		
		model.addAttribute("cityList", cityRepository.getAllCity(id));

		return "normal/add_contact_form";
	}
	
	
	
	
	
}
