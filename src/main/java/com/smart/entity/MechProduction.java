package com.smart.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class MechProduction {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int mechId;
	private String name;

	private int modelNo;
	private Date mfgDate;
	private String finalizerName;
	private String countryName;
	private String stateName;
	private String cityName;

	@ManyToOne
	@JsonIgnore
	private User user;

	public MechProduction() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MechProduction(int mechId, String name, int modelNo, Date mfgDate, String finalizerName, String countryName,
			String stateName, String cityName, User user) {
		super();
		this.mechId = mechId;
		this.name = name;
		this.modelNo = modelNo;
		this.mfgDate = mfgDate;
		this.finalizerName = finalizerName;
		this.countryName = countryName;
		this.stateName = stateName;
		this.cityName = cityName;
		this.user = user;
	}

	public int getMechId() {
		return mechId;
	}

	public void setMechId(int mechId) {
		this.mechId = mechId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getModelNo() {
		return modelNo;
	}

	public void setModelNo(int modelNo) {
		this.modelNo = modelNo;
	}

	public Date getMfgDate() {
		return mfgDate;
	}

	public void setMfgDate(Date mfgDate) {
		this.mfgDate = mfgDate;
	}

	public String getFinalizerName() {
		return finalizerName;
	}

	public void setFinalizerName(String finalizerName) {
		this.finalizerName = finalizerName;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "MechProduction [mechId=" + mechId + ", name=" + name + ", modelNo=" + modelNo + ", mfgDate=" + mfgDate
				+ ", finalizerName=" + finalizerName + ", countryName=" + countryName + ", stateName=" + stateName
				+ ", cityName=" + cityName + ", user=" + user + "]";
	}

}
