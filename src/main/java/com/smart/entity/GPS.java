package com.smart.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class GPS {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long loactionId;
	private int userId;
	private double latitude;
	private double longitude;
	private long timestamp;
	public GPS(long loactionId, int userId, double latitude, double longitude, long timestamp) {
		super();
		this.loactionId = loactionId;
		this.userId = userId;
		this.latitude = latitude;
		this.longitude = longitude;
		this.timestamp = timestamp;
	}
	public GPS() {
		super();
		// TODO Auto-generated constructor stub
	}
	public long getLoactionId() {
		return loactionId;
	}
	public void setLoactionId(long loactionId) {
		this.loactionId = loactionId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	
	

}
