package com.smart.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.dto.CollegeDto;
import com.smart.entity.College;
import com.smart.entity.User;

public interface CollegeRepo extends JpaRepository<College, Integer> {

	List<College> findByNameContaining(String name);

	Page<College> findByNameContainingIgnoreCase(String searchTerm, PageRequest pageable);

	

	@Query("SELECT c FROM College c " +
		       "WHERE LOWER(c.name) LIKE %:searchTerm% " +
		       "OR LOWER(c.contactNumber) LIKE %:searchTerm% " +
		       "OR LOWER(c.address) LIKE %:searchTerm% " +
		       "OR LOWER(c.email) LIKE %:searchTerm%")
	    Page<College> searchCollegesByAnyField(@Param("searchTerm") String searchTerm, Pageable pageable);

	Page<College> findByNameContainingOrEmailContainingOrContactNumberContainingOrAddressContaining(String searchTerm,
			String searchTerm2, String searchTerm3, String searchTerm4, Pageable pageable);

	College findByEmail(String email);

	List<College> findByUser(User currentUser);

	Page<College> findByUser(User currentUser, Pageable pageable);

//	@Query("SELECT c FROM College c WHERE c.user = :user AND " +
//	           "(c.name LIKE %:searchTerm% OR c.email LIKE %:searchTerm% OR " +
//	           "c.contactNumber LIKE %:searchTerm% OR c.address LIKE %:searchTerm% OR " +
//	           "c.col6 LIKE %:searchTerm% OR c.col7 LIKE %:searchTerm%)")
//	Page<College> searchByUserAndTerm(User user, String searchTerm, Pageable pageable);
	
	
	@Query("SELECT c FROM College c WHERE c.user = :user AND " +
	           "(c.name LIKE %:searchTerm% OR c.email LIKE %:searchTerm% OR " +
	           "c.contactNumber LIKE %:searchTerm% OR c.address LIKE %:searchTerm% OR " +
	           "c.col1 LIKE %:searchTerm% OR c.col2 LIKE %:searchTerm%)")
	Page<College> searchByUserAndTerm(User user, String searchTerm, Pageable pageable);

	List<College> findAllByUser(User currentUser);

}
