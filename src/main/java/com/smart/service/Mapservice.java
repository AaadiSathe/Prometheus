package com.smart.service;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smart.dao.GPSRepository;
import com.smart.dao.UserRepository;
import com.smart.entity.GPS;
import com.smart.entity.User;

@Service
@RequestMapping("/map")
public class Mapservice {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private GPSRepository gpsRepository;
    
    @GetMapping("/search/{lat}/{long}")
    @ResponseBody
    public String addLocation(@PathVariable("lat") String lat, @PathVariable("long") String longitude,Principal principal) {
        System.err.println("coming");
        String username=principal.getName();
        User user=    userRepository.getUserByUserName(username);
        
        System.err.println("double lat "+Double.parseDouble(lat));
        System.err.println("double long "+Double.parseDouble(longitude));
        
        GPS newLocation=new GPS();
        newLocation.setLatitude(Double.parseDouble(lat));
        newLocation.setLongitude(Double.parseDouble(longitude));
        newLocation.setUserId(user.getId());
        newLocation.setTimestamp(System.currentTimeMillis());
        
        gpsRepository.save(newLocation);
        
        return "Data received: Lat=" + lat + ", Long=" + longitude;
    }
}
