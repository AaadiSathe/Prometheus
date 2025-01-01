package com.smart.entity;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.util.Date;
@Entity
public class Reminder {
	
	
	  @Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private Long id;

	    private String message;

	    @Temporal(TemporalType.TIMESTAMP)
	    private Date dateTime;
	    @ManyToOne
	    @JoinColumn(name = "user_id")
	    @JsonBackReference
	    private User user;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public Date getDateTime() {
			return dateTime;
		}

		public void setDateTime(Date dateTime) {
			this.dateTime = dateTime;
		}

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}

}
