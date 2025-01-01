package com.smart.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Cities {
	@Id
	private Integer id;
	private String name;
	
	@OneToOne
	@JoinColumn(name="state_id")
	private States states;
	
	public Cities() {}
	
	public Cities(Integer id, String name, States states) {
		super();
		this.id = id;
		this.name = name;
		this.states = states;
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
	
	
	public States getStates() {
		return states;
	}
	public void setStates(States states) {
		this.states = states;
	}
	@Override
	public String toString() {
		return "City [id=" + id + ", name=" + name + "]";
	}
	
	
}
