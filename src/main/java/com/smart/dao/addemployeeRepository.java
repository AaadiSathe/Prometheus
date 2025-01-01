package com.smart.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.entity.Addemployee;
import com.smart.entity.Addemployee;
import com.smart.entity.Contact;
import com.smart.entity.User;

@Repository
public interface addemployeeRepository  extends JpaRepository<Addemployee, Integer>{

	@Query("from Addemployee as e where e.user.id=:userId")
	//current page
	//contact per page
	public Page<Addemployee> findEmployeeByUser(@Param("userId")int userId,Pageable pePageable);	
	
	@Query("SELECT e FROM Addemployee e WHERE UPPER(e.name) LIKE UPPER(CONCAT('%', :name, '%')) AND e.user = :user")
	public List<Addemployee> findByNameContainAndUser(@Param("name") String name, @Param("user") User user);
	
	
	@Query("SELECT e FROM Addemployee e WHERE UPPER(e.name) LIKE UPPER(CONCAT('%', :name, '%')) AND e.user = :user")
	public Page<Addemployee> findByNameContainAndUser(@Param("name") String name, @Param("user") User user, Pageable pageable);
	
	List<Addemployee> findByDepartment(String department, Pageable pageable);

	
	Page<Addemployee> findByUserAndDepartmentAndNameContaining(User user, String department, String name, Pageable pageable);
	
	Page<Addemployee> findByUserAndNameContaining(User user, String name, Pageable pageable);

    // Method to find employees by user and department only
    Page<Addemployee> findByUserAndDepartment(User user, String department, Pageable pageable);

    // Method to find employees by user only
    Page<Addemployee> findEmployeeByUser(Integer userId, Pageable pageable);
	
	
}
