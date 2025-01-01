package com.smart.service;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.smart.dao.MechProductionRepository;
import com.smart.entity.MechProduction;

@Service
public class MechProductionService {

	@Autowired
	private MechProductionRepository mechProductionRepository;

	public MechProduction save(MechProduction production) {
		return mechProductionRepository.save(production);
	}

	public Page<MechProduction> findAll(int page, int size ) {
		Pageable pageable=PageRequest.of(page, size);
		return mechProductionRepository.findAll(pageable);
	}

	public void deleteById(int id) {
		mechProductionRepository.deleteById(id);
	}

	public MechProduction findById(int id) {
		return mechProductionRepository.findById(id).orElse(null);
	}
	public Page<MechProduction> findByNameContainingIgnoreCase(String name, int page, int size) {
		Pageable pageable= PageRequest.of(page, size);
		return mechProductionRepository.findByNameContainingIgnoreCase(name, pageable);
	}
	
	public List<MechProduction> findByName(String name){
	return mechProductionRepository.findByNameContainingIgnoreCase(name);
	}
	
	}
	


