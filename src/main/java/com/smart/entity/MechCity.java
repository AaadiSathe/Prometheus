package com.smart.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class MechCity {
	@Id
	@Column(name = "city_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int cityId;

	@Column(name = "city_name")
	private String cityName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "state_id")
	@JsonBackReference
	private MechState mechState;

	public MechCity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MechCity(int cityId, String cityName, MechState mechState) {
		super();
		this.cityId = cityId;
		this.cityName = cityName;
		this.mechState = mechState;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public MechState getMechState() {
		return mechState;
	}

	public void setMechState(MechState mechState) {
		this.mechState = mechState;
	}

	@Override
	public String toString() {
		return "MechCity [cityId=" + cityId + ", cityName=" + cityName + "]";
	}

}
