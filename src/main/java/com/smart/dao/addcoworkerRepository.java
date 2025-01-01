package com.smart.dao;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.entity.Coworker;
import com.smart.entity.User;


@Repository
public interface addcoworkerRepository extends JpaRepository<Coworker, Integer>  {

	 @Query("from Coworker as c where c.user.id=:userId")
		public Page<Coworker> findCoworkerByUser(@Param("userId") int userId,Pageable pageble);
		
	     @Query("SELECT c from Coworker c WHERE UPPER(c.name)  LIKE UPPER(CONCAT('%', :name,'%')) AND c.user =:user")
	     public List<Coworker> findByNameContainingAndUser(@Param("name") String name,@Param("user") User user);
	     
	     @Query("SELECT c from Coworker c WHERE UPPER(c.name)  LIKE UPPER(CONCAT('%', :name,'%')) AND c.user =:user")
	     public Page<Coworker> findByNameContainingAndUser(@Param("name") String name,@Param("user") User user,Pageable pageable);
	     
	     public List<Coworker> searchCoworkerByName(String name);
	     
		public Page<Coworker> findByNameContainingAndDropdownvalueContainingAndUser(String name, String dropdownvalue,
				User user, Pageable pageable);

		public Page<Coworker> findByDropdownvalueContainingAndUser(String dropdownvalue, User user, Pageable pageable);
	     
}
