
	
	package com.smart.dao;

	import java.util.List;

	import org.springframework.data.domain.Page;
	import org.springframework.data.domain.Pageable;
	import org.springframework.data.jpa.repository.JpaRepository;
	import org.springframework.data.jpa.repository.Query;
	import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

	
	import com.smart.entity.Addcustomer;
	import com.smart.entity.Contact;
	import com.smart.entity.User;

	@Repository
	public interface  addcustomerRepository extends JpaRepository<Addcustomer, Integer>{

		//jpa query
		@Query("from Addcustomer as e where e.user.id=:userId")
		//current page
		//contact per page
		public Page<Addcustomer> findCustomerByUser(@Param("userId")int userId,Pageable pePageable);	
		
		@Query("SELECT e FROM Addcustomer e WHERE UPPER(e.name) LIKE UPPER(CONCAT('%', :name, '%')) AND e.user = :user")
		public List<Addcustomer> findByNameContainAndUser(@Param("name") String name, @Param("user") User user);
		
		
		@Query("SELECT e FROM Addcustomer e WHERE UPPER(e.name) LIKE UPPER(CONCAT('%', :name, '%')) AND e.user = :user")
		public Page<Addcustomer> findByNameContainAndUser(@Param("name") String name, @Param("user") User user, Pageable pageable);

	
		//jpa query
				@Query("from Addcustomer as e where e.user.id=:userId")
				//current page
				//contact per page
				public Addcustomer findCustomerByUserUsingId(@Param("userId")int userId);	

		
		
	}


