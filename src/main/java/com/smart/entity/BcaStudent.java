package com.smart.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date; // Import java.util.Date

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class BcaStudent {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int Bid;
    private String name;
    private String email;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dob;        
    private String collagename;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;      
    private String image;
    private String education;

    @ManyToOne
    @JsonIgnore
    private User user;

    public BcaStudent() {
    }

    public BcaStudent(int bid, String name, String email, Date dob, String collagename, 
                      Date startDate, Date endDate, String education, String image, User user) {
        super();
        this.Bid = bid;
        this.name = name;
        this.email = email;
        this.dob = dob;
        this.collagename = collagename;
        this.startDate = startDate;
        this.endDate = endDate;
        this.image = image;
        this.education = education;
        this.user = user;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getBid() {
        return Bid;
    }

    public void setBid(int bid) {
        Bid = bid;
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

    public Date getDob() {      // Changed return type to Date
        return dob;
    }

    public void setDob(Date dob) {   // Changed parameter type to Date
        this.dob = dob;
    }

    public String getCollagename() {
        return collagename;
    }

    public void setCollagename(String collagename) {
        this.collagename = collagename;
    }

    public Date getStartDate() {   // Changed return type to Date
        return startDate;
    }

    public void setStartDate(Date startDate) {   // Changed parameter type to Date
        this.startDate = startDate;
    }

    public Date getEndDate() {     // Changed return type to Date
        return endDate;
    }

    public void setEndDate(Date endDate) {      // Changed parameter type to Date
        this.endDate = endDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }
}