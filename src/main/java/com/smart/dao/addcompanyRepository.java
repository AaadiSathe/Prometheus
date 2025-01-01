package com.smart.dao;
import java.util.List;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.entity.Addcompany;
import com.smart.entity.User;

@Repository
public interface addcompanyRepository extends JpaRepository<Addcompany, Integer> {

	@Query("from Addcompany as c where c.user.id=:userId")
	
	public Page<Addcompany> findCompanyByUser(@Param("userId")int userId,Pageable pePageable);
	

	@Query("SELECT c FROM Addcompany c WHERE UPPER(c.companyName) LIKE UPPER(CONCAT('%', :name, '%')) AND c.user = :user")
	public List<Addcompany> findByNameContainingAndUser(@Param("name") String name, @Param("user") User user);
	
	
	@Query("SELECT c FROM Addcompany c WHERE UPPER(c.companyName) LIKE UPPER(CONCAT('%', :name, '%')) AND c.user = :user")
	public Page<Addcompany> findByNameContainingAndUser(@Param("name") String name, @Param("user") User user, Pageable pageable);

	@Query("SELECT c FROM Addcompany c WHERE c.id = :id")
    Addcompany findById(int id);
	
	@Query("SELECT c FROM Addcompany c WHERE LOWER(c.companyName) LIKE LOWER(CONCAT('%', :name, '%')) AND c.user = :user")
   public Page<Addcompany> findByNameContainingIgnoreCase(@Param("name") String companyName, Pageable pageable);


	

	


}