package com.smart.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Addemployee {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int employeeid;
	private String name;
	@Column(unique=true)
	private String email;
	private  String address;
	private  String mobileno;
	private String department;
	
	
	@ManyToOne
	@JsonIgnore
	private User user;
	
	public Addemployee() {}

	public int getEmployeeid() {
		return employeeid;
	}

	public void setEmployeeid(int employeeid) {
		this.employeeid = employeeid;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMobileno() {
		return mobileno;
	}

	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	@Override
	public String toString() {
		return "Addemployee [employeeid=" + employeeid + ", name=" + name + ", email=" + email + ", address=" + address
				+ ", mobileno=" + mobileno + ", department=" + department + ", user=" + user + "]";
	}

	public Addemployee(int employeeid, String name, String email, String address, String mobileno, String department,
			User user) {
		super();
		this.employeeid = employeeid;
		this.name = name;
		this.email = email;
		this.address = address;
		this.mobileno = mobileno;
		this.department = department;
		this.user = user;
	}
	
	
}
