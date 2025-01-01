package com.smart.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.smart.entity.MechCountry;
@Repository
public interface MechCountryRepository extends JpaRepository<MechCountry, Integer>{

}
