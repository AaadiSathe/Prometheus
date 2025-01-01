package com.smart.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.entity.School;
import com.smart.entity.User;

public interface SchoolRepo extends JpaRepository<School, Integer> {

	
//	@Query("SELECT s.customColumns FROM School s WHERE s.user = :user")
//	List<Map<String, String>> findCustomColumnsByUser( @Param("user") User user);
	
	List<School> findByUser(User user);

	List<School> findAllByUser(User currentUser);

}
