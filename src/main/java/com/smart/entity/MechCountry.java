package com.smart.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class MechCountry {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "country_id")
	int countryId;

	@Column(name = "country_name")
	private String countryName;

	@OneToMany(mappedBy = "mechCountry", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonManagedReference
	private List<MechState> mechStates;

	public MechCountry() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MechCountry(int countryId, String countryName, List<MechState> mechStates) {
		super();
		this.countryId = countryId;
		this.countryName = countryName;
		this.mechStates = mechStates;
	}

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	
	  public List<MechState> getMechStates() { return mechStates; }
	  
	  public void setMechStates(List<MechState> mechStates) { this.mechStates =
	  mechStates; }

	@Override
	public String toString() {
		return "MechCountry [countryId=" + countryId + ", countryName=" + countryName + "]";
	}
	  
	 

}