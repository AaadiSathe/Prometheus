package com.smart.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smart.entity.ColumnMetadata;
import com.smart.entity.User;

public interface ColumnMetadataRepo extends JpaRepository<ColumnMetadata, Integer> {

	//ColumnMetadata findByOriginalName(String originalName);
	
	Optional<ColumnMetadata> findByOriginalName(String originalName);

	List<ColumnMetadata> findByUser(User currentUser);

	

	List<ColumnMetadata> findAllByOriginalNameAndUser(String columnName, User currentUser);
	
	Optional<ColumnMetadata> findByOriginalNameAndUser(String columnName, User currentUser);

}
