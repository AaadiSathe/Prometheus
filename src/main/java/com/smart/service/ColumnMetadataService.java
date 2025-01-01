package com.smart.service;

import java.util.List;
import java.util.Map;

import com.smart.dto.ColumnMetadataDto;
import com.smart.entity.User;

public interface ColumnMetadataService {
	
	
	//String getRenamedColumnName(String originalName);
	
	void saveRenamedColumnName(String originalName, String renamedName);
	
	Map<String, String> getAllRenamedColumnNames();

	void saveRenamedColumnName(String originalName, String renamedName, User currentUser);

	Map<String, String> getAllRenamedColumnNames(User currentUser);

	void removeRenamedColumnName(String string, User currentUser);

	void saveOrUpdateColumns(List<ColumnMetadataDto> columns, User currentUser);

	

}
