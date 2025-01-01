package com.smart.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Coworker {

	
	@Id
	@GeneratedValue(strategy =GenerationType.AUTO )
	private int coworkerid;
	private String name;
	private String address;
	private String email;
	private String mobileno;
	private String dropdownvalue;
	@ManyToOne
	@JsonIgnore
	private User user;
	public Coworker() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Coworker(int coworkerid, String name, String address, String email, String mobileno, String dropdownvalue,
			User user) {
		super();
		this.coworkerid = coworkerid;
		this.name = name;
		this.address = address;
		this.email = email;
		this.mobileno = mobileno;
		this.dropdownvalue = dropdownvalue;
		this.user = user;
	}

	public int getCoworkerid() {
		return coworkerid;
	}
	public void setCoworkerid(int coworkerid) {
		this.coworkerid = coworkerid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobileno() {
		return mobileno;
	}
	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}
	public String getDropdownvalue() {
		return dropdownvalue;
	}
	public void setDropdownvalue(String dropdownvalue) {
		this.dropdownvalue = dropdownvalue;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Coworker [coworkerid=" + coworkerid + ", name=" + name + ", address=" + address + ", email=" + email
				+ ", mobileno=" + mobileno + ", dropdownvalue=" + dropdownvalue + ", user=" + user + "]";
	}

	
	

	
	
	
	
}
