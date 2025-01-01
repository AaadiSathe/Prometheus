package com.smart.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Vendor {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int vendorid;

	private String name;

	private String mobile;

	@Column(unique = true)
	private String email;

	@Column(length = 500)
	private String address;

	private String products;

	private String businessType;

	public Vendor() {
		super();
	}

	public Vendor(int vendorid, String name, String mobile, String email, String address, String products,
			String businessType, String dateOfRegistration, User user) {
		super();
		this.vendorid = vendorid;
		this.name = name;
		this.mobile = mobile;
		this.email = email;
		this.address = address;
		this.products = products;
		this.businessType = businessType;
		this.dateOfRegistration = dateOfRegistration;
		this.user = user;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	private String dateOfRegistration;

	@ManyToOne
	@JsonIgnore
	private User user;

	public int getVendorid() {
		return vendorid;
	}

	public void setVendorid(int vendorid) {
		this.vendorid = vendorid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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

	public String getProducts() {
		return products;
	}

	public void setProducts(String products) {
		this.products = products;
	}

	public String getDateOfRegistration() {
		return dateOfRegistration;
	}

	public void setDateOfRegistration(String dateOfRegistration) {
		this.dateOfRegistration = dateOfRegistration;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
