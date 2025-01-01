package com.smart.service;

import java.util.List;
import java.util.Map;

import com.smart.dto.CustomColumnsDTO;
import com.smart.entity.School;
import com.smart.entity.User;

public interface SchoolService {

	School addSchool(School school, User currentUser);

	School addSchool(School school);

	Map<String, String> getAllCustomColumns(User currentUser);

	void updateCustomColumns(CustomColumnsDTO customColumnDTO, User currentUser);

	List<School> getAllSchools(User currentUser);

}
