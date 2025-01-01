package com.smart.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smart.dao.CollegeRepo;
import com.smart.dao.ColumnMetadataRepo;
import com.smart.dto.ColumnMetadataDto;
import com.smart.entity.College;
import com.smart.entity.ColumnMetadata;
import com.smart.entity.User;

@Service
public class ColumnMetadataServiceImpl implements ColumnMetadataService {

	@Autowired
	private ColumnMetadataRepo columnMetadataRepo;
	
	@Autowired
	private CollegeRepo collegeRepo;

	@Override
	public void saveRenamedColumnName(String originalName, String renamedName) {
		Optional<ColumnMetadata> optionalMetadata = columnMetadataRepo.findByOriginalName(originalName);
		ColumnMetadata columnMetadata;
		if (optionalMetadata.isPresent()) {
			columnMetadata = optionalMetadata.get();
		} else {
			columnMetadata = new ColumnMetadata();
			columnMetadata.setOriginalName(originalName);
		}
		columnMetadata.setRenamedName(renamedName);
		columnMetadataRepo.save(columnMetadata);

	}

	@Override
	public Map<String, String> getAllRenamedColumnNames() {
		List<ColumnMetadata> columnMetadataList = columnMetadataRepo.findAll();
		Map<String, String> renamedColumns = new HashMap<>();
		for (ColumnMetadata metadata : columnMetadataList) {
			renamedColumns.put(metadata.getOriginalName(), metadata.getRenamedName());
		}
		return renamedColumns;
	}

	@Override
	public void saveRenamedColumnName(String originalName, String renamedName, User currentUser) {

		Optional<ColumnMetadata> colMetadataOpt = columnMetadataRepo.findByOriginalNameAndUser(originalName, currentUser);
		
		ColumnMetadata columnMetadata;
		if(colMetadataOpt.isPresent()) {
			columnMetadata= colMetadataOpt.get();
			columnMetadata.setRenamedName(renamedName);
		}else {
			columnMetadata = new ColumnMetadata();
			columnMetadata.setOriginalName(originalName);
			columnMetadata.setRenamedName(renamedName);
			columnMetadata.setUser(currentUser);
		}
		
		columnMetadataRepo.save(columnMetadata);
	}

	@Override
	public Map<String, String> getAllRenamedColumnNames(User currentUser) {

		List<ColumnMetadata> metadataList = columnMetadataRepo.findByUser(currentUser);
		System.out.println(metadataList);
		Map<String, String> renamedColumns = new LinkedHashMap<>();
		for (ColumnMetadata metadata : metadataList) {
			renamedColumns.put(metadata.getOriginalName(), metadata.getRenamedName());
		}
		
		System.out.println(renamedColumns.toString());
		return renamedColumns;
	}

	@Override
	public void removeRenamedColumnName(String columnName, User currentUser) {
		// Check if the renamed column exists for the current user
		 List<ColumnMetadata> columnMetadataList = columnMetadataRepo.findAllByOriginalNameAndUser(columnName, currentUser);
		    if (!columnMetadataList.isEmpty()) {
		        // Delete all found column metadata
		        columnMetadataRepo.deleteAll(columnMetadataList);
		    }
	}

	@Override
	public void saveOrUpdateColumns(List<ColumnMetadataDto> columns, User currentUser) {
		
		List<ColumnMetadata> existingColumns = columnMetadataRepo.findByUser(currentUser);
		
		Set<String> processedNames = new HashSet<>();
		
		for(ColumnMetadataDto dto : columns) {
			boolean found = false;
			for(ColumnMetadata existing : existingColumns) {
				if(existing.getOriginalName().equals(dto.getOriginalName())) {
					
					
					existing.setRenamedName(dto.getRenamedName());
					columnMetadataRepo.save(existing);
					found=true;
					processedNames.add(dto.getOriginalName());
					break;
				}
			}
			
			if(!found) {
				ColumnMetadata newMetadata = new ColumnMetadata();
				newMetadata.setOriginalName(dto.getOriginalName());
				newMetadata.setRenamedName(dto.getRenamedName());
				newMetadata.setUser(currentUser);
				columnMetadataRepo.save(newMetadata);
				processedNames.add(dto.getOriginalName());
			}
		}
		
		// remove unprocessed or unselected columns
		for(ColumnMetadata existing : existingColumns) {
			if(!processedNames.contains(existing.getOriginalName())) {
				columnMetadataRepo.delete(existing);
			}
		}
		
	}
	
	// new code trying some new changes
//	@Override
//	public void saveOrUpdateColumns(List<ColumnMetadataDto> columns, User currentUser) {
//		
//		List<ColumnMetadata> existingColumns = columnMetadataRepo.findByUser(currentUser);
//		
//		Set<String> processedNames = new HashSet<>();
//		
//		for(ColumnMetadataDto dto : columns) {
//			boolean found = false;
//			for(ColumnMetadata existing : existingColumns) {
//				if(existing.getOriginalName().equals(dto.getOriginalName())) {
//					
//					List<College> updateList= collegeRepo.findAllByUser(currentUser);
//					
//					for(College c : updateList) {
//						
//					}
//					
//					existing.setRenamedName(dto.getRenamedName());
//					columnMetadataRepo.save(existing);
//					found=true;
//					processedNames.add(dto.getOriginalName());
//					break;
//				}
//			}
//			
//			if(!found) {
//				ColumnMetadata newMetadata = new ColumnMetadata();
//				newMetadata.setOriginalName(dto.getOriginalName());
//				newMetadata.setRenamedName(dto.getRenamedName());
//				newMetadata.setUser(currentUser);
//				columnMetadataRepo.save(newMetadata);
//				processedNames.add(dto.getOriginalName());
//			}
//		}
//		
//		// remove unprocessed or unselected columns
//		for(ColumnMetadata existing : existingColumns) {
//			if(!processedNames.contains(existing.getOriginalName())) {
//				columnMetadataRepo.delete(existing);
//			}
//		}
//		
//	}

	
	
//	@Override
//	public void saveOrUpdateColumns(List<ColumnMetadataDto> columns, User currentUser) {
//        // Fetch the current user using Principal
//       
//        List<ColumnMetadata> existingColumns = columnMetadataRepo.findByUser(currentUser);
//
//        Map<String, ColumnMetadata> existingMap = existingColumns.stream()
//                .collect(Collectors.toMap(ColumnMetadata::getOriginalName, Function.identity()));
//
//        for (ColumnMetadataDto dto : columns) {
//            if (existingMap.containsKey(dto.getOriginalName())) {
//                ColumnMetadata existing = existingMap.get(dto.getOriginalName());
//                existing.setRenamedName(dto.getRenamedName());
//                columnMetadataRepo.save(existing);
//                existingMap.remove(dto.getOriginalName());
//            } else {
//                ColumnMetadata newMetadata = new ColumnMetadata();
//                newMetadata.setOriginalName(dto.getOriginalName());
//                newMetadata.setRenamedName(dto.getRenamedName());
//                newMetadata.setUser(currentUser);
//                columnMetadataRepo.save(newMetadata);
//            }
//        }
//
//        // Remove any columns that were unchecked (not in the incoming list)
//        for (ColumnMetadata remaining : existingMap.values()) {
//            columnMetadataRepo.delete(remaining);
//        }
//    }

	

}
