package com.smart.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.entity.States;



@Repository
public interface StateRepository extends JpaRepository<States,Integer> {
	
	
	
	
	
	 @Query("SELECT c from States as c WHERE c.countries.id=:id") 
	 public List<States> getAllState(@Param("id")int id);

	
	 
	 
	 
	 
	
	
}
