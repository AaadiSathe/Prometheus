package com.smart.entity;






import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import javax.persistence.OneToOne;

@Entity
public class States {
	@Id
	private Integer id;
	private String name;
	@OneToOne
	@JoinColumn(name="country_id")
	private Countries countries;
	
	
	public States() {}
	
	public States(Integer id, String name, Countries countries) {
		super();
		this.id = id;
		this.name = name;
		this.countries = countries;
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
	public Countries getCountries() {
		return countries;
	}
	public void setCountries(Countries countries) {
		this.countries = countries;
	}
	
	
	
	
	
	
}
