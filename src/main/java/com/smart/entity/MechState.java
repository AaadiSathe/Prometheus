package com.smart.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
@Entity
public class MechState {
    @Id
    @Column(name = "state_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    int stateId;
    
    @Column(name = "state_name")
	private String stateName;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    @JsonBackReference
    private MechCountry mechCountry;
	
	@OneToMany(mappedBy = "mechState", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonManagedReference
    private List<MechCity> mechCity;

	public MechState() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MechState(int stateId, String stateName,MechCountry mechCountry, List<MechCity> mechCity) {
		super();
		this.stateId = stateId;
		this.stateName = stateName;
		this.mechCountry = mechCountry;
		this.mechCity = mechCity;
	}

	public int getStateId() {
		return stateId;
	}

	public void setStateId(int stateId) {
		this.stateId = stateId;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public MechCountry getMechCountry() {
		return mechCountry;
	}

	public void setMechCountry(MechCountry mechCountry) {
		this.mechCountry = mechCountry;
	}

	public List<MechCity> getMechCity() {
		return mechCity;
	}

	public void setMechCity(List<MechCity> mechCity) {
		this.mechCity = mechCity;
	}

	@Override
	public String toString() {
		return "MechState [stateId=" + stateId + ", stateName=" + stateName +"]";
	}

	


	

}
