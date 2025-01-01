package com.smart.dao;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smart.entity.MechCity;

@Repository
public interface MechCityRepository extends JpaRepository<MechCity, Integer> {

	List<MechCity> findByMechState_StateName(String stateName);

}
