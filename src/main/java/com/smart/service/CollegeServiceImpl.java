package com.smart.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.smart.dao.CollegeRepo;
import com.smart.dto.CollegeDto;
import com.smart.entity.College;
import com.smart.entity.User;

@Service
public class CollegeServiceImpl implements CollegeService {

	@Autowired
	private CollegeRepo collegeRepo;

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private ColumnMetadataService columnMetadataService;

	@Override
	public College addCollege(College college) {

		return collegeRepo.save(college);
	}

	@Override
	public void deleteCollege(Integer id) {
		collegeRepo.deleteById(id);

	}

	public Page<College> getAllColleges(Pageable pageable) {
		return collegeRepo.findAll(pageable);
	}

	@Override
	public College getCollegeById(Integer id) {
		return collegeRepo.findById(id).orElse(null);
	}

	@Override
	public void updateCollege(College college) {
		collegeRepo.save(college);

	}

	@Override
	public List<College> searchCollegeByName(String name) {
		return collegeRepo.findByNameContaining(name);
	}

	@Override
	public Page<College> findPaginated(int page, int pageSize) {
		return collegeRepo.findAll(PageRequest.of(page, pageSize));
	}

//	@Override
//	public List<College> searchCollegeByAnyField(String searchTerm) {
//	    // Convert the search term to lowercase for case-insensitive search
//	    String searchTermLowerCase = searchTerm.toLowerCase();
//
//	    // Retrieve all colleges from the repository
//	    List<College> allColleges = collegeRepo.findAll();
//
//	    // Create a list to store the matched colleges
//	    List<College> matchedColleges = new ArrayList<>();
//
//	    // Loop through each college and check if any field contains the search term
//	    for (College college : allColleges) {
//	        // Check if any field of the college contains the search term
//	        if (college.getName().toLowerCase().contains(searchTermLowerCase) ||
//	            college.getAddress().toLowerCase().contains(searchTermLowerCase) ||
//	            college.getContactNumber().toLowerCase().contains(searchTermLowerCase) ||
//	            college.getEmail().toLowerCase().contains(searchTermLowerCase)) {
//	            // If any field matches, add the college to the list of matched colleges
//	            matchedColleges.add(college);
//	        }
//	    }
//
//	    // Return the list of matched colleges
//	    return matchedColleges;
//	}

	@Override
	public List<College> searchCollegeByAnyField(String searchTerm) {
		String jpql = "SELECT c FROM College c WHERE LOWER(c.name) LIKE LOWER(:searchTerm) OR "
				+ "LOWER(c.address) LIKE LOWER(:searchTerm) OR " + "LOWER(c.contactNumber) LIKE LOWER(:searchTerm) OR "
				+ "LOWER(c.email) LIKE LOWER(:searchTerm)";

		Query query = entityManager.createQuery(jpql, College.class);
		query.setParameter("searchTerm", "%" + searchTerm + "%");

		return query.getResultList();
	}

	@Override
	public Page<College> getColleges(int page) {
		int pageSize = 5; // Page size is set to 5
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        return collegeRepo.findAll(pageRequest);
	    
	}

	@Override
	public Page<College> searchColleges(String searchTerm, int page, int size) {
		

		return collegeRepo.findByNameContainingIgnoreCase(searchTerm, PageRequest.of(page, size));
		    
	}

	@Override
	public Page<College> searchCollegeByAnyFieldPaginated(String searchTerm, int page, int pageSize) {
		Pageable pageable = PageRequest.of(page, pageSize);

        // Perform a search query using the provided search term, searching by any field
        return collegeRepo.searchCollegesByAnyField(searchTerm, pageable);
	}

	@Override
    public Page<College> searchCollegeByAnyField(String searchTerm, Pageable pageable) {
		System.out.println("check-----------");
        // Perform the search using the college repository
        return collegeRepo.findByNameContainingOrEmailContainingOrContactNumberContainingOrAddressContaining(
                searchTerm, searchTerm, searchTerm, searchTerm, pageable);
    }

	@Override
	public boolean isEmailExists(String email) {
		
		return collegeRepo.findByEmail(email) != null;
	}

	@Override
	public void saveCollege(College college) {
		collegeRepo.save(college);
		
	}

	@Override
	public List<CollegeDto> getAllColleges() {
		 List<College> colleges = collegeRepo.findAll();
	        Map<String, String> renamedColumns = columnMetadataService.getAllRenamedColumnNames();

	        return colleges.stream().map(college -> {
	            CollegeDto dto = new CollegeDto();
	            dto.setId(college.getId());
	            dto.setName(college.getName());
	            dto.setContactNumber(college.getContactNumber());
	            dto.setEmail(college.getEmail());
	            dto.setAddress(college.getAddress());
	            dto.setCol1(college.getCol1());
	            dto.setCol2(college.getCol2());
	            dto.setCol3(college.getCol3());
	            dto.setCol4(college.getCol4());
	            dto.setCol5(college.getCol5());
	            

	         //   dto.setCol6Renamed(renamedColumns.getOrDefault("col6", "Column 6"));
	         //   dto.setCol7Renamed(renamedColumns.getOrDefault("col7", "Column 7"));
	            return dto;
	        }).collect(Collectors.toList());
	}

	@Override
	public List<College> getAllColleges(User currentUser) {
		
		return collegeRepo.findByUser(currentUser);
	}

	@Override
	public Page<College> getAllColleges(User currentUser, String searchTerm, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<College> collegePage;
		
		
		
		if(searchTerm != null && !searchTerm.isEmpty()) {
			//System.out.println("in search term present");
			collegePage = collegeRepo.searchByUserAndTerm(currentUser,searchTerm,pageable);
			
		}else {
			//System.out.println("in search term not present");
			
			collegePage = collegeRepo.findByUser(currentUser,pageable);
		}
		return collegePage;
	}


	
}
