package com.smart.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.smart.dto.CollegeDto;
import com.smart.entity.College;
import com.smart.entity.User;

public interface CollegeService {

	College addCollege(College college);

	void deleteCollege(Integer id);

	Page<College> getAllColleges(Pageable pageable);

	College getCollegeById(Integer id);

	void updateCollege(College college);

	List<College> searchCollegeByName(String name);

	Page<College> findPaginated(int page, int pageSize);

	List<College> searchCollegeByAnyField(String searchTerm);

	Page<College> getColleges(int page);

	Page<College> searchColleges(String searchTerm, int page, int size);

	Page<College> searchCollegeByAnyFieldPaginated(String search_term, int page, int pageSize);

	Page<College> searchCollegeByAnyField(String searchTerm, Pageable pageable);

	boolean isEmailExists(String email);
	
	
	void saveCollege(College college);
	
	List<CollegeDto> getAllColleges();

	List<College> getAllColleges(User currentUser);

	Page<College> getAllColleges(User currentUser, String searchTerm, int page, int size);
	
	

}
