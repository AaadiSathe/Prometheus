package com.smart.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String name;
//	@Column(unique = true)
	private String email;
	private String password;
	@Column(length = 500)
	private String about;
	private String role;
	private boolean enabled;
	private String imageUrl;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,mappedBy = "user")
	private List<Contact>  contact=new ArrayList<>(); 
//	
//	public List<Vendor> getVendors() {
//		return vendors;
//	}
//	public void setVendors(List<Vendor> vendors) {
//		this.vendors = vendors;
//	}
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
	@JsonManagedReference
	private List<Reminder> reminders = new ArrayList<>();

	public List<Reminder> getReminders() {
	    return reminders;
	}

	public void setReminders(List<Reminder> reminders) {
	    this.reminders = reminders;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,mappedBy = "user")
	private List<Addemployee>  addemployee=new ArrayList<>(); 
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,mappedBy = "user")
	private List<Addcompany>  addecompany=new ArrayList<>(); 
	
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,mappedBy = "user")
	private List<Addcustomer>  addecustomer=new ArrayList<>(); 
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
	private List<Vendor> vendors = new ArrayList<>();
	

	public List<Vendor> getVendors() {
		return vendors;
	}
	public void setVendors(List<Vendor> vendors) {
		this.vendors = vendors;
	}
	
	

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,mappedBy = "user")
	private List<Coworker> coworkers  =new ArrayList<>(); 
	
	public List<Coworker> getCoworkers() {
		return coworkers;
	}
	public void setCoworkers(List<Coworker> coworkers) {
		this.coworkers = coworkers;
	}
	public List<Addcustomer> getAddecustomer() {
		return addecustomer;
	}
	public void setAddecustomer(List<Addcustomer> addecustomer) {
		this.addecustomer = addecustomer;
	}
	
	public List<Addcompany> getAddecompany() {
		return addecompany;
	}
	public void setAddecompany(List<Addcompany> addecompany) {
		this.addecompany = addecompany;
	}
	
	
	public List<Contact> getContact() {
		return contact;
	}
	public void setContact(List<Contact> contact) {
		this.contact = contact;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,mappedBy = "user")
	private List<Addstudent>  student=new ArrayList<>(); 
	
	public List<Addstudent> getStudent() {
		return student;
		
	}
	public void setStudent(List<Addstudent> student) {
		this.student = student;
	}	
	
	public List<Addemployee> getAddemployee() {
		return addemployee;
	}
	public void setAddemployee(List<Addemployee> addemployee) {
		this.addemployee = addemployee;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,mappedBy = "user")
	private List<MechProduction>  mechProduction=new ArrayList<>();
	public List<MechProduction> getMechProduction() {
		return mechProduction;
	}
	public void setMechProduction(List<MechProduction> mechProduction) {
		this.mechProduction = mechProduction;
	}
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	public User(int id, String name, String email, String password, String about, String role, boolean enabled,
			String imageUrl) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.about = about;
		this.role = role;
		this.enabled = enabled;
		this.imageUrl = imageUrl;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", about=" + about
				+ ", role=" + role + ", enabled=" + enabled + ", imageUrl=" + imageUrl + ", contact=" + contact + "]";
	}
	
	
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,mappedBy = "user")
	private List<BcaStudent> bcaStudent=new ArrayList<>();	
	
	
	
	public List<BcaStudent> getBcaStudents() {
		// TODO Auto-generated method stub
		return bcaStudent;
	}
	
	
}
