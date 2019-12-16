package it.unical.demacs.asde.signme.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Invitation")
public class Subscription {

	@Id
	private Integer subscriptionId;
	private String student;
	private Integer course;

	public Subscription() {
		super();
	}

	public Subscription(Integer subscriptionId, String student, Integer course) {
		super();
		this.subscriptionId = subscriptionId;
		this.student = student;
		this.course = course;
	}

	public Integer getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(Integer subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public Integer getCourse() {
		return course;
	}

	public void setCourse(Integer course) {
		this.course = course;
	}

	public String getStudent() {
		return student;
	}

	public void setStudent(String student) {
		this.student = student;
	}

}
