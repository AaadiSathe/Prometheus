package com.smart.entity;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class School {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String name;

	private String ContactNumber;

	private String email;

	private String address;

	@ManyToOne
	@JsonIgnore
	private User user;

	// custom columns
	@ElementCollection
	@MapKeyColumn(name = "column_key")
	@Column(name = "column_value")
	@CollectionTable(name = "school_custom_column_values", joinColumns = @JoinColumn(referencedColumnName = "id"))
	private Map<String, String> customColumns = new LinkedHashMap<>();

	public School() {
		super();
	}

	public School(Integer id, String name, String contactNumber, String email, String address, User user,
			Map<String, String> customColumns) {
		super();
		this.id = id;
		this.name = name;
		ContactNumber = contactNumber;
		this.email = email;
		this.address = address;
		this.user = user;
		this.customColumns = customColumns;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContactNumber() {
		return ContactNumber;
	}

	public void setContactNumber(String contactNumber) {
		ContactNumber = contactNumber;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Map<String, String> getCustomColumns() {
		return customColumns;
	}

	public void setCustomColumns(Map<String, String> customColumns) {
		this.customColumns = customColumns;
	}

}
