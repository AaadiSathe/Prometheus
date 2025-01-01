package com.smart.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.entity.Cities;



@Repository
public interface CityRepositoy extends JpaRepository<Cities,Integer> {
	
	
	 @Query("FROM Cities as c WHERE c.states.id=:id") 
	 public List<Cities> getAllCity(@Param("id")int id);
	 
	 
}
