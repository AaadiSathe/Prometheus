package com.smart.dao;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smart.entity.MechState;

@Repository
public interface MechStateRepository extends JpaRepository<MechState, Integer> {
	
	List<MechState> findByMechCountry_CountryName(String countryName);

}
