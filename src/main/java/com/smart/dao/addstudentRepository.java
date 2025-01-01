package com.smart.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.entity.Addemployee;
import com.smart.entity.Addstudent;

import com.smart.entity.User;
@Repository
public interface addstudentRepository extends JpaRepository<Addstudent, Integer>{
	
	@Query("from Addstudent as c where c.user.id=:userId")
	//current page
	//contact per page
	public Page<Addstudent> findStudentByUser(@Param("userId")int userId,Pageable pePageable);
	
	@Query("SELECT c FROM Addstudent c WHERE UPPER(c.name) LIKE UPPER(CONCAT('%', :name, '%')) AND c.user = :user")
	public List<Addstudent> findByNameContainingAndUser(@Param("name") String name, @Param("user") User user);
	
	
	@Query("SELECT c FROM Addstudent c WHERE UPPER(c.name) LIKE UPPER(CONCAT('%', :name, '%')) AND c.user = :user")
	public Page<Addstudent> findByNameContainingAndUser(@Param("name") String name, @Param("user") User user, Pageable pageable);

	public Page<Addstudent> searchByAddressContaining(String address, Pageable pageable);

	/*
	 * @Query("DELETE FROM Addstudent a WHERE a.studentid = :studentid") void
	 * deleteStudent(@Param("studentid") int studentid);
	 */
	

}
