package com.smart.entity;


import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;





@Entity
public class Addcompany {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int id;

    private String companyName;
    private String chemicalName;
 
    private Date manufacturerDate;

    private String assignedBy;
    @Transient
    private int dropdownvalue;

    
    @ManyToOne
	@JsonIgnore
    private User user;


   
    // Default constructor (required by JPA)
    public Addcompany() {
    	super();
    }

    // Parameterized constructor
    public Addcompany(String companyName, String chemicalName, Date manufacturerDate, String assignedBy,User user,int  dropdownvalue) {
        this.companyName = companyName;
        this.chemicalName = chemicalName;
        this.manufacturerDate = manufacturerDate;
        this.assignedBy = assignedBy;
        this.user=user;
        this.dropdownvalue=dropdownvalue;
    }

    // Getters and setters (for all fields)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getChemicalName() {
        return chemicalName;
    }

    public void setChemicalName(String chemicalName) {
        this.chemicalName = chemicalName;
    }

    public Date getManufacturerDate() {
        return manufacturerDate;
    }

    public void setManufacturerDate(Date manufacturerDate) {
        this.manufacturerDate = manufacturerDate;
    }

    public String getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(String assignedBy) {
        this.assignedBy = assignedBy;
    }
    
    

    // toString method (for debugging and logging)
    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", companyName='" + companyName + '\'' +
                ", chemicalName='" + chemicalName + '\'' +
                ", manufacturerDate=" + manufacturerDate +
                ", assignedBy='" + assignedBy + '\'' +
                ", assignedBy='" + assignedBy + '\'' +
                ", dropdownvalue='" + dropdownvalue + '\'' +
                '}';
    }

	public int getDropdownvalue() {
		return dropdownvalue;
	}

	public void setDropdownvalue(int dropdownvalue) {
		this.dropdownvalue = dropdownvalue;
	}

}
