package com.smart.entity;





import javax.persistence.Entity;
import javax.persistence.Id;



@Entity
public class Countries {
	@Id
	private Integer id;
	private String shortName;
	private String name;
	private int phoneCode;
	
	public Countries() {}
	
	
	public Countries(Integer id, String shortName, String name, int phoneCode) {
		super();
		this.id = id;
		this.shortName = shortName;
		this.name = name;
		this.phoneCode = phoneCode;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public int getPhoneCode() {
		return phoneCode;
	}
	public void setPhoneCode(int phoneCode) {
		this.phoneCode = phoneCode;
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


	@Override
	public String toString() {
		return "Countries [id=" + id + ", shortName=" + shortName + ", name=" + name + ", phoneCode=" + phoneCode + "]";
	}
	
	
	
}
