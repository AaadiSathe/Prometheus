package com.smart.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Addcustomer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int id;
	
	private String companyName;
	
	
	private String  mobileno;
	private String email;
	private String name;
	
	@ManyToOne
	@JsonIgnore
	private User user; // for colum n join
	
	// default constructor
	public Addcustomer() {}
	
	
	//para meterized constr
	


	public int getId() {
		return id;
	}


	public Addcustomer(int id, String companyName, String mobileno, String email, String name, User user) {
		super();
		this.id = id;
		this.companyName = companyName;
		this.mobileno = mobileno;
		this.email = email;
		this.name = name;
		this.user = user;
	}


	public void setId(int id) {
		this.id = id;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getCompanyName() {
		return companyName;
	}


	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}


	public String getMobileno() {
		return mobileno;
	}


	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	@Override
	public String toString() {
		return "Addcustomer [companyName=" + companyName + ", mobileno=" + mobileno + ", email=" + email + ", user="
				+ user + ", name=" + name + "]";
	}


	
	
	
	
}


