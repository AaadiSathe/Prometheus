package com.smart.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.entity.Addemployee;
import com.smart.entity.User;
import com.smart.entity.Vendor;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Integer>{
	
	
	
	@Query("from Vendor as v where v.user.id=:userId")
	public Page<Vendor> findVendorByUser(@Param("userId")int id, Pageable page);
	
	@Query("SELECT v FROM Vendor v WHERE UPPER(v.name) LIKE UPPER(CONCAT('%', :name, '%')) AND v.user=:user")
	public List<Vendor> findByNameContainAndUser(@Param("name") String name, @Param("user") User user);
	
	@Query("SELECT v FROM Vendor v WHERE UPPER(v.name) LIKE UPPER(CONCAT('%', :name, '%')) AND v.user=:user")
	public Page<Vendor> findByNameContainAndUser(@Param("name") String name, @Param("user") User user, Pageable page);
	
	@Query("SELECT v FROM Vendor v WHERE LOWER(v.name) LIKE LOWER(CONCAT('%', :name, '%'))")
	public List<Vendor> findVendorByNameContainingIgnoreCase(String name);

	boolean existsByEmail(String email);

}
