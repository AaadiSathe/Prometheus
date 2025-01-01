package com.smart.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class ColumnMetadata {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String originalName;
	private String renamedName;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "user_id")
	private User user;

	public ColumnMetadata() {
		super();
	}

	

	public ColumnMetadata(Integer id, String originalName, String renamedName) {
		super();
		this.id = id;
		this.originalName = originalName;
		this.renamedName = renamedName;
	}

	


	public Integer getId() {
		return id;
	}



	public void setId(Integer id) {
		this.id = id;
	}



	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	public String getRenamedName() {
		return renamedName;
	}

	public void setRenamedName(String renamedName) {
		this.renamedName = renamedName;
	}
	
	

	public User getUser() {
		return user;
	}



	public void setUser(User user) {
		this.user = user;
	}



	@Override
	public String toString() {
		return "ColumnMetadata [id=" + id + ", originalName=" + originalName + ", renamedName=" + renamedName + "]";
	}
	
	

}
