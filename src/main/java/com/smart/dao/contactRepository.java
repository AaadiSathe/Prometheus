package com.smart.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.entity.Addstudent;
import com.smart.entity.Contact;
import com.smart.entity.User;
@Repository
public interface contactRepository extends JpaRepository<Contact, Integer> {

	//using pegination
	
	@Query("from Contact as c where c.user.id=:userId")
	//current page
	//contact per page
	public Page<Contact> findContactByUser(@Param("userId")int userId,Pageable pePageable);
	
//	//this is use to get the list of contact only
//	@Query("from Contact as c where c.user.id=:userId")
//	public List<Contact> findContactByUser(@Param("userId")int userId);
	
	@Query("SELECT c FROM Contact c WHERE UPPER(c.Name) LIKE UPPER(CONCAT('%', :name, '%')) AND c.user = :user")
	public List<Contact> findByNameContainingAndUser(@Param("name") String name, @Param("user") User user);
	
	
	@Query("SELECT c FROM Contact c WHERE UPPER(c.Name) LIKE UPPER(CONCAT('%', :name, '%')) AND c.user = :user")
	public Page<Contact> findByNameContainingAndUser(@Param("name") String name, @Param("user") User user, Pageable pageable);




}
