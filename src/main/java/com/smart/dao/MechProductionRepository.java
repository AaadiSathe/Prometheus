package com.smart.dao;


import java.util.List;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smart.entity.MechProduction;
@Repository
public interface MechProductionRepository extends JpaRepository<MechProduction, Integer> {

//	List<MechProduction> findByNameContainingIgnoreCase(String name);

    Page<MechProduction> findAll(Pageable pageable);

    Page<MechProduction> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    List<MechProduction> findByNameContainingIgnoreCase(String name);
}
