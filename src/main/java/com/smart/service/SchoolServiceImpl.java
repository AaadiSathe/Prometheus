package com.smart.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smart.dao.SchoolRepo;
import com.smart.dto.CustomColumnsDTO;
import com.smart.entity.School;
import com.smart.entity.User;

@Service
public class SchoolServiceImpl implements SchoolService{
	
	@Autowired
	private SchoolRepo schoolRepo;

	@Override
	public School addSchool(School school, User currentUser) {
		
		school.setUser(currentUser);
		
		return schoolRepo.save(school);
	}

	@Override
	public School addSchool(School school) {
		
		return schoolRepo.save(school);
	}

	@Override
	public Map<String, String> getAllCustomColumns(User currentUser) {
		List<School> schools = schoolRepo.findByUser(currentUser);
		Map<String, String> customColumns = new LinkedHashMap<>();
		for(School school : schools) {
			customColumns.putAll(school.getCustomColumns());
		}
		
		return customColumns;
	}

	@Override
	public void updateCustomColumns(CustomColumnsDTO customColumnDTO, User currentUser) {
		List<School> schools = schoolRepo.findByUser(currentUser);
		
		for(School school : schools) {
			Map<String, String> customColumns = school.getCustomColumns();
			
			//add new columns and keep existing one
			customColumnDTO.getColumnsToSave().forEach(column -> customColumns.putIfAbsent(column.getCustomColumnName(),""));
			
			//remove specified columns
			customColumnDTO.getColumnsToDelete().forEach(column -> customColumns.remove(column.getCustomColumnName()));
			
			school.setCustomColumns(customColumns);
			schoolRepo.save(school);
		}
		
	}

	@Override
	public List<School> getAllSchools(User currentUser) {
		List<School> schools = schoolRepo.findByUser(currentUser);
		return schools;
	}
	
//	@Override
//	public void updateCustomColumns(CustomColumnsDTO customColumnDTO, User currentUser) {
//	    List<School> schools = schoolRepo.findByUser(currentUser);
//	    
//	    if (schools.isEmpty()) {
//	        // Create a new dummy school object
//	        School dummySchool = new School();
//	        dummySchool.setUser(currentUser);
//	        dummySchool.setName("Dummy School");
//	        dummySchool.setAddress("Dummy Address");
//	        dummySchool.setCustomColumns(new LinkedHashMap<>());
//
//	        // Add new columns
//	        customColumnDTO.getColumnsToSave().forEach(column -> dummySchool.getCustomColumns().putIfAbsent(column.getCustomColumnName(), ""));
//
//	        // Save the new dummy school object
//	        schoolRepo.save(dummySchool);
//	    } else {
//	        // Check for a dummy school
//	        boolean hasDummySchool = schools.size() == 1 && "Dummy School".equals(schools.get(0).getName());
//
//	        if (hasDummySchool) {
//	            // Remove the dummy school
//	            schoolRepo.delete(schools.get(0));
//	            schools.remove(0);
//
////	            // Add a new school from the backend
////	            School newSchool = new School();
////	            newSchool.setUser(currentUser);
////	            // Set the new school's fields from the backend data, e.g., name and address
////	            newSchool.setName("New School Name");
////	            newSchool.setAddress("New School Address");
////	            newSchool.setCustomColumns(new LinkedHashMap<>());
////
////	            // Add new columns
////	            customColumnDTO.getColumnsToSave().forEach(column -> newSchool.getCustomColumns().putIfAbsent(column.getCustomColumnName(), ""));
////
////	            // Save the new school object
////	            schoolRepo.save(newSchool);
//	        } else {
//	            for (School school : schools) {
//	                Map<String, String> customColumns = school.getCustomColumns();
//
//	                // Add new columns and keep existing ones
//	                customColumnDTO.getColumnsToSave().forEach(column -> customColumns.putIfAbsent(column.getCustomColumnName(), ""));
//
//	                // Remove specified columns
//	                customColumnDTO.getColumnsToDelete().forEach(column -> customColumns.remove(column.getCustomColumnName()));
//
//	                school.setCustomColumns(customColumns);
//	                schoolRepo.save(school);
//	            }
//	        }
//	    }
//	}


}
