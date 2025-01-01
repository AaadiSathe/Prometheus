package com.smart.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.entity.BcaStudent;
import com.smart.entity.User;
@Repository
public interface BcaStudentRepositry extends JpaRepository<BcaStudent, Integer>{

	Page<BcaStudent> findByNameContaining(String name, Pageable pageable);
    
	Page<BcaStudent> findByUser(User user, Pageable pageable);

	Page<BcaStudent> findByNameContainingAndUser(String studentName, User user, Pageable pageable);

	Page<BcaStudent> findByUser(int id, Pageable pageable);
	@Query("SELECT s FROM BcaStudent s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))")
	Page<BcaStudent> findByNameContainingIgnoreCase(@Param("name") String name, Pageable pageable);
	
	 // Method to filter by education
    Page<BcaStudent> findByEducation(String education, Pageable pageable);

    // Method to search by name
    

    // Method to search by name and filter by education
    Page<BcaStudent> findByNameContainingAndEducation(String name, String education, Pageable pageable);
 //   Page<BcaStudent> findByCollageName(String collagename, Pageable pageable);


	 

	

}

